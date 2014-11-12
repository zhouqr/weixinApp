import java.util.ArrayList;
import java.util.List;

import models.User;
import play.Play;
import play.db.jpa.GenericModel.JPAQuery;
import play.db.jpa.JPABase;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {
	@Override
	public void doJob() throws Exception {
		if(User.count()==0 && Play.mode.isDev())
		{
			Fixtures.deleteAllModels();
			Fixtures.loadModels("init-data.yml");
		}
	}
}
