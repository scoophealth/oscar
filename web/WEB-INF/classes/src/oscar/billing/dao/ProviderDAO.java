package oscar.billing.dao;

import oscar.billing.model.Provider;

import oscar.oscarDB.DBHandler;

import oscar.util.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Properties;


public class ProviderDAO extends DAO {
    public ProviderDAO(Properties pvar) throws SQLException {
        super(pvar);
    }

    public ArrayList list(int type) throws SQLException {
        ArrayList list = new ArrayList();
        String sql = "select provider_no, last_name, first_name from provider";

        if (type == Provider.DOCTOR) {
            sql = sql + " where provider_type = 'doctor'";
        } else if (type == Provider.RECEPTIONIST) {
            sql = sql + " where provider_type = 'receptionist'";
        }

        sql = sql + " order by first_name, last_name";

        DBHandler db = getDb();

        try {
            ResultSet rs = db.GetSQL(sql);
            System.out.println("sql " + sql);
            
            Provider provider = new Provider();
            provider.setProviderNo("0");
			provider.setLastName("");
			provider.setFirstName("Todos");
			list.add(provider);

            while (rs.next()) {
                provider = new Provider();
                provider.setProviderNo(rs.getString(1));
                provider.setLastName(rs.getString(2));
                provider.setFirstName(rs.getString(3));
                list.add(provider);
            }
        } finally {
            db.CloseConn();
        }

        return list;
    }
}
