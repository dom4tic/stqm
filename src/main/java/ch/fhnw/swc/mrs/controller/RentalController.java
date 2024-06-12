package ch.fhnw.swc.mrs.controller;

import ch.fhnw.swc.mrs.api.MRSServices;
import ch.fhnw.swc.mrs.controller.dto.RentalDto;
import ch.fhnw.swc.mrs.model.Rental;
import ch.fhnw.swc.mrs.util.JsonUtil;
import ch.fhnw.swc.mrs.util.StatusCodes;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.io.Serial;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import static ch.fhnw.swc.mrs.util.JsonUtil.dataToJson;
import static ch.fhnw.swc.mrs.util.JsonUtil.jsonToData;
import static ch.fhnw.swc.mrs.util.RequestUtil.getParamId;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;

public final class RentalController {

    private static MRSServices backend;

    public static void init(MRSServices services) {
        if (services == null) {
            throw new IllegalArgumentException("Backend component missing");
        }
        backend = services;
        JsonUtil.registerSerializer(new RentalController.RentalSerializer());
        JsonUtil.registerDeserializer(RentalDto.class, new RentalController.RentalDtoDeserializer());

        get("/rentals",        RentalController.getAllRentals);
        delete("/rentals/:id", RentalController.deleteRental);
        post("/rentals",       RentalController.createRental);
    }

    private static Route getAllRentals = (Request request, Response response) -> {
        Collection<Rental> rentals;
        rentals = backend.getAllRentals();

        return dataToJson(rentals);
    };

    private static Route deleteRental = (Request request, Response response) -> {
        long id = getParamId(request);
        if (backend.deleteRental(id)) {
            response.status(StatusCodes.NO_CONTENT);
        } else {
            response.status(StatusCodes.NOT_FOUND);
        }
        return "";
    };

    private static Route createRental = (Request request, Response response) -> {

        String json = request.body();

        RentalDto rentalDto = (RentalDto) jsonToData(json, RentalDto.class);
        String body = "";
        try {
            Rental r = backend.createRental(
                rentalDto.getUser().getUserid(),
                rentalDto.getMovie().getMovieid(),
                rentalDto.getRentalDate()
            );
            body = dataToJson(r);
            response.status(StatusCodes.CREATED);
        } catch (Exception e) {
            halt(StatusCodes.NOT_FOUND, e.getMessage());
        }
        return body;
    };

    // prevent instantiation
    private RentalController() {
    }

    /**
     * Helper class to serialize the Rental Object to json
     */
    private static class RentalSerializer extends StdSerializer<Rental> {

        RentalSerializer() {
            super(Rental.class);
        }

        @Override
        public void serialize(Rental r, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeStartObject();
            jgen.writeNumberField("id",         r.getRentalId());
            jgen.writeNumberField("userId",     r.getUser().getUserid());
            jgen.writeNumberField("movieId",    r.getMovie().getMovieid());
            jgen.writeStringField("rentalDate", r.getRentalDate().format(DateTimeFormatter.ISO_DATE));
            jgen.writeEndObject();
        }

    }

    /**
     * Helper class to deserialize the Rental Object from json
     */
    private static class RentalDtoDeserializer extends StdDeserializer<RentalDto> {

        protected RentalDtoDeserializer(Class<?> vc) {
            super(vc);
        }

        @Serial
        private static final long serialVersionUID = 1L;

        RentalDtoDeserializer() {
            this(null);
        }

        @Override
        public RentalDto deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonNode node = jp.getCodec().readTree(jp);

            long userId               = node.get("userId").asLong();
            long movieId              = node.get("movieId").asLong();
            String rentalDate         = node.get("rentalDate").asText();
            LocalDate localRentalDate = LocalDate.parse(rentalDate, DateTimeFormatter.ISO_DATE);

            RentalDto r = new RentalDto(backend.getUserById(userId), backend.getMovieById(movieId), localRentalDate);

            if (node.get("id") != null) {
                long id = node.get("id").asLong();
                r.setId(id);
            }
            return r;
        }
    }


}
