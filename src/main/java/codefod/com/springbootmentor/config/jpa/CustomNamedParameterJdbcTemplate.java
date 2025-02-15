package codefod.com.springbootmentor.config.jpa;

import org.slf4j.MDC;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SuppressWarnings("all")
public class CustomNamedParameterJdbcTemplate extends NamedParameterJdbcTemplate {

    private static String HOST_NAME;

    static {
        try {
            HOST_NAME = InetAddress.getLocalHost().getHostName();
        } catch (Exception ignored) {
        }
    }

    public CustomNamedParameterJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    public CustomNamedParameterJdbcTemplate(JdbcOperations classicJdbcTemplate) {
        super(classicJdbcTemplate);
    }

    @Override
    @Nullable
    public <T> T execute(String sql, SqlParameterSource paramSource,
                         PreparedStatementCallback<T> action)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.execute(sql, paramSource, action);
    }

    @Override
    @Nullable
    public <T> T execute(String sql, Map<String, ?> paramMap, PreparedStatementCallback<T> action)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.execute(sql, paramMap, action);
    }

    @Override
    @Nullable
    public <T> T execute(String sql, PreparedStatementCallback<T> action)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.execute(sql, action);
    }

    @Override
    @Nullable
    public <T> T query(String sql, SqlParameterSource paramSource, ResultSetExtractor<T> rse)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.query(sql, paramSource, rse);
    }

    @Override
    @Nullable
    public <T> T query(String sql, Map<String, ?> paramMap, ResultSetExtractor<T> rse)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.query(sql, paramMap, rse);
    }

    @Override
    @Nullable
    public <T> T query(String sql, ResultSetExtractor<T> rse) throws DataAccessException {
        sql = addTraceId(sql);
        return super.query(sql, rse);
    }

    @Override
    public void query(String sql, SqlParameterSource paramSource, RowCallbackHandler rch)
            throws DataAccessException {
        sql = addTraceId(sql);
        super.query(sql, paramSource, rch);
    }

    @Override
    public void query(String sql, Map<String, ?> paramMap, RowCallbackHandler rch)
            throws DataAccessException {
        sql = addTraceId(sql);
        super.query(sql, paramMap, rch);
    }

    @Override
    public void query(String sql, RowCallbackHandler rch) throws DataAccessException {
        sql = addTraceId(sql);
        super.query(sql, rch);
    }

    @Override
    public <T> List<T> query(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.query(sql, paramSource, rowMapper);
    }

    @Override
    public <T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.query(sql, paramMap, rowMapper);
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
        sql = addTraceId(sql);
        return super.query(sql, rowMapper);
    }

    private static String addTraceId(String sql) {
        sql = sql + " /* " + HOST_NAME + " ; trace_id: " + MDC.get("traceId") + " */";
        return sql;
    }

    @Override
    public <T> Stream<T> queryForStream(String sql, SqlParameterSource paramSource,
                                        RowMapper<T> rowMapper)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.queryForStream(sql, paramSource, rowMapper);
    }

    @Override
    public <T> Stream<T> queryForStream(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.queryForStream(sql, paramMap, rowMapper);
    }

    @Override
    @Nullable
    public <T> T queryForObject(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.queryForObject(sql, paramSource, rowMapper);
    }

    @Override
    @Nullable
    public <T> T queryForObject(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.queryForObject(sql, paramMap, rowMapper);
    }

    @Override
    @Nullable
    public <T> T queryForObject(String sql, SqlParameterSource paramSource, Class<T> requiredType)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.queryForObject(sql, paramSource, requiredType);
    }

    @Override
    @Nullable
    public <T> T queryForObject(String sql, Map<String, ?> paramMap, Class<T> requiredType)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.queryForObject(sql, paramMap, requiredType);
    }

    @Override
    public Map<String, Object> queryForMap(String sql, SqlParameterSource paramSource)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.queryForMap(sql, paramSource);
    }

    @Override
    public Map<String, Object> queryForMap(String sql, Map<String, ?> paramMap)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.queryForMap(sql, paramMap);
    }

    @Override
    public <T> List<T> queryForList(String sql, SqlParameterSource paramSource,
                                    Class<T> elementType)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.queryForList(sql, paramSource, elementType);
    }

    @Override
    public <T> List<T> queryForList(String sql, Map<String, ?> paramMap, Class<T> elementType)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.queryForList(sql, paramMap, elementType);
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, SqlParameterSource paramSource)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.queryForList(sql, paramSource);
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, Map<String, ?> paramMap)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.queryForList(sql, paramMap);
    }

    @Override
    public SqlRowSet queryForRowSet(String sql, SqlParameterSource paramSource)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.queryForRowSet(sql, paramSource);
    }

    @Override
    public SqlRowSet queryForRowSet(String sql, Map<String, ?> paramMap)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.queryForRowSet(sql, paramMap);
    }

    @Override
    public int update(String sql, SqlParameterSource paramSource) throws DataAccessException {
        sql = addTraceId(sql);
        return super.update(sql, paramSource);
    }

    @Override
    public int update(String sql, Map<String, ?> paramMap) throws DataAccessException {
        sql = addTraceId(sql);
        return super.update(sql, paramMap);
    }

    @Override
    public int update(String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.update(sql, paramSource, generatedKeyHolder);
    }

    @Override
    public int update(
            String sql, SqlParameterSource paramSource, KeyHolder generatedKeyHolder,
            @Nullable String[] keyColumnNames)
            throws DataAccessException {
        sql = addTraceId(sql);
        return super.update(sql, paramSource, generatedKeyHolder, keyColumnNames);
    }

    @Override
    public int[] batchUpdate(String sql, Map<String, ?>[] batchValues) {
        sql = addTraceId(sql);
        return super.batchUpdate(sql, batchValues);
    }

    @Override
    public int[] batchUpdate(String sql, SqlParameterSource[] batchArgs) {
        sql = addTraceId(sql);
        return super.batchUpdate(sql, batchArgs);
    }
}