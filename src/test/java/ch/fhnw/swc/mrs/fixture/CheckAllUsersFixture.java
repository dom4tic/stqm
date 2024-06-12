package ch.fhnw.swc.mrs.fixture;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.fhnw.swc.mrs.data.SimpleMRSServices;
import ch.fhnw.swc.mrs.model.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fit.RowFixture;


public class CheckAllUsersFixture extends RowFixture {
    private SimpleMRSServices mrsServices = MRSServicesFactory.getInstance();
    private Logger logger = LogManager.getLogger(CheckAllUsersFixture.class);

    @Override
    public Object[] query() throws Exception {


        Collection<User> users = mrsServices.getAllUsers();

        List<ATUser> atusers = new ArrayList<ATUser>(users.size());

        logger.debug("Users: " + users.size());

        for (User u: users) {
            logger.debug(u.getName());
            atusers.add(new ATUser(u.getName(), u.getFirstName(), u.getBirthdate()));
        }
        return atusers.toArray();

    }

    @Override
    public Class<ATUser> getTargetClass() {
        return ATUser.class;
    }
}

