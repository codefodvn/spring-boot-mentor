package codefod.com.springbootmentor.config.hibernate;

import codefod.com.springbootmentor.util.LogUtil;
import java.net.InetAddress;
import org.hibernate.resource.jdbc.spi.StatementInspector;

public class SqlCommentStatementInspector implements StatementInspector {

    private static String HOST_NAME;

    static {
        try {
            HOST_NAME = InetAddress.getLocalHost().getHostName();
        } catch (Exception ignored) {
        }
    }

    @Override
    public String inspect(String sql) {
        sql = sql + " /* " + HOST_NAME + " ; trace_id: " + LogUtil.getCorrelationId() + " */";
        return sql;
    }
}
