package todoer.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class Database {

    private static final Logger LOG = LoggerFactory.getLogger(Database.class);

    public static DataSource buildDataSource() {
        final BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");

        String jdbcUrl = System.getenv("JDBC_DATABASE_URL");
        LOG.info("JDBC_DATABASE_URL is " + jdbcUrl);
        if (jdbcUrl != null) {
            ds.setUrl(jdbcUrl);
            return ds;
        }

        String devUrl = System.getenv("JDBC_TODO_DEV_URL");
        LOG.info("JDBC_TODO_DEV_URL is " + devUrl);

        if (devUrl == null) {
            throw new RuntimeException("No matching environmental variables for JDBC_DATABASE_URL or JDBC_TODO_DEV_URL");
        } else {
            ds.setUrl(devUrl);
            return ds;
        }
    }

    public static void runFlywayMigrations() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(buildDataSource());
        flyway.repair();
        flyway.migrate();
    }

    public static DSLContext buildDSLContext() {
        return DSL.using(buildDataSource(), SQLDialect.POSTGRES_9_4);
    }
}
