package codefod.com.springbootmentor.common.constant;

public class MDCConstant {

    private MDCConstant() {
        throw new IllegalStateException("Utility class");
    }

    public static final String CHANNEL_CODE = "channelCode";
    public static final String TRACE_ID = "traceId";
    public static final String SPAN_ID = "spanId";
    public static final String REQUEST_REMOTE_HOST_MDC_KEY = "req.remoteHost";
    public static final String REQUEST_AT = "req.at";
    public static final String REQUEST_TIME = "req.time";
    public static final String REQUEST_USER_AGENT_MDC_KEY = "req.userAgent";
    public static final String CLIENT_IP = "req.clientIP";
    public static final String REQUEST_REQUEST_URI = "req.requestURI";
    public static final String TYPE = "log.type";
    public static final String REQUEST_CLIENT_ID = "req.clientId";
    public static final String REQUEST_QUERY_STRING = "req.queryString";
    public static final String REQUEST_REQUEST_URL = "req.requestURL";
    public static final String REQUEST_METHOD = "req.method";
    public static final String REQUEST_ACTION_CODE = "req.actionCode";
    public static final String REQUEST_ERROR_CODE = "errorCode";
    public static final String REQUEST_X_FORWARDED_FOR = "req.xForwardedFor";
    public static final String REQUEST_RESPONSE_HTTP_STATUS = "req.httpStatus";
    public static final String REQUEST_RESPONSE_AT = "req.resAt";
    public static final String REQUEST_RESPONSE_SIZE = "req.resSize";
    public static final String REQUEST_SIZE = "req.size";

}
