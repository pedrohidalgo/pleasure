package com.qualixium.playnb.filetype.conf;

import com.qualixium.playnb.filetype.conf.completion.ConfigurationItem;
import java.util.ArrayList;
import java.util.List;

public class ConfLanguageHelper {

    public static List<ConfigurationItem> getKeys() {
        List<ConfigurationItem> items = new ArrayList<>();
        items.add(new ConfigurationItem("session.cookieName", "The default name for the cookie is PLAY_SESSION. This can be changed by configuring the key session.cookieName in application.conf", "https://www.playframework.com/documentation/2.4.x/ScalaSessionFlash#How-it-is-different-in-Play"));
        items.add(new ConfigurationItem("session.maxAge", "You can also set the maximum age of the session cookie by configuring the key session.maxAge (in milliseconds) in application.conf", "https://www.playframework.com/documentation/2.4.x/ScalaSessionFlash#How-it-is-different-in-Play"));
        items.add(new ConfigurationItem("play.http.parser.maxMemoryBuffer", "By default, the maximum content length that they will parse is 100KB. It can be overridden by specifying the play.http.parser.maxMemoryBuffer property in application.conf", "https://www.playframework.com/documentation/2.4.x/ScalaBodyParsers#Max-content-length"));
        items.add(new ConfigurationItem("play.http.parser.maxDiskBuffer", "For parsers that buffer content on disk, such as the raw parser or multipart/form-data, the maximum content length is specified using the play.http.parser.maxDiskBuffer property, it defaults to 10MB", "https://www.playframework.com/documentation/2.4.x/ScalaBodyParsers#Max-content-length"));
        items.add(new ConfigurationItem("play.http.errorHandler", "If you don’t want to place your error handler in the root package, or if you want to be able to configure different error handlers for different\n"
                + "environments, you can do this by configuring the play.http.errorHandler configuration property in application.conf", "https://www.playframework.com/documentation/2.4.x/ScalaErrorHandling#Supplying-a-custom-error-handler"));
        items.add(new ConfigurationItem("play.http.filters", "The Filters class can either be in the root package, or if it has another name or is in another package, needs to be configured using\n"
                + "play.http.filters in application.conf: play.http.filters = \"filters.MyFilters\"", "https://www.playframework.com/documentation/2.4.x/ScalaHttpFilters#Using-filters"));
        items.add(new ConfigurationItem("db.default.driver", "Database Driver class name: org.postgresql.Driver", "https://www.playframework.com/documentation/2.4.x/ScalaDatabase#Configuring-JDBC-connection-pools"));
        items.add(new ConfigurationItem("db.default.url", "Database connection url: jdbc:postgresql://database.example.com/playdb\"", "https://www.playframework.com/documentation/2.4.x/ScalaDatabase#Configuring-JDBC-connection-pools"));
        items.add(new ConfigurationItem("db.default.username", "dbuser", "https://www.playframework.com/documentation/2.4.x/ScalaDatabase#Configuring-JDBC-connection-pools"));
        items.add(new ConfigurationItem("db.default.password", "a strong password", "https://www.playframework.com/documentation/2.4.x/ScalaDatabase#Configuring-JDBC-connection-pools"));
        items.add(new ConfigurationItem("db.default.jndiName", "JPA requires the datasource to be accessible via JNDI. You can expose any Play-managed datasource via JNDI by adding this configuration in\n"
                + "conf/application.conf", "https://www.playframework.com/documentation/2.4.x/JavaDatabase#Exposing-the-datasource-through-JNDI"));
        items.add(new ConfigurationItem("play.db.pool", "Out of the box, Play provides two database connection pool implementations, HikariCP\n"
                + "and BoneCP. The default is HikariCP, but this can\n"
                + "be changed by setting the play.db.pool property:\n"
                + "play.db.pool=bonecp", "https://www.playframework.com/documentation/2.4.x/JavaDatabase#Selecting-and-configuring-the-connection-pool"));
        items.add(new ConfigurationItem("play.cache.bindCaches", "If you want to access multiple different ehcache caches, then you’ll need to tell Play to bind them in application.conf, like so:\n"
                + "play.cache.bindCaches = [\"db-cache\", \"user-cache\", \"session-cache\"]", "https://www.playframework.com/documentation/2.4.x/JavaCache#Accessing-different-caches"));
        items.add(new ConfigurationItem("play.ws.followRedirects", "Configures the client to follow 301 and 302 redirects (default is true).", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-WS"));
        items.add(new ConfigurationItem("play.ws.useProxyProperties", "To use the system http proxy settings(http.proxyHost, http.proxyPort) (default is true)", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-WS"));
        items.add(new ConfigurationItem("play.ws.useragent", "To configure the User-Agent header field.", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-WS"));
        items.add(new ConfigurationItem("play.ws.compressionEnabled", "Set it to true to use gzip/deflater encoding (default is false).", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-WS"));
        items.add(new ConfigurationItem("play.ws.timeout.connection", "The maximum time to wait when connecting to the remote host (default is 120 seconds).", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-Timeouts"));
        items.add(new ConfigurationItem("play.ws.timeout.idle", "The maximum time the request can stay idle (connection is established but waiting for more data) (default is 120 seconds).", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-Timeouts"));
        items.add(new ConfigurationItem("play.ws.timeout.request", "The total time you accept a request to take (it will be interrupted even if the remote host is still sending data) (default is 120 seconds).", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-Timeouts"));
        items.add(new ConfigurationItem("play.ws.ning.allowPoolingConnection", "Set true if connection can be pooled by a ConnectionsPool.", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-AsyncHttpClientConfig"));
        items.add(new ConfigurationItem("play.ws.ning.allowSslConnectionPool", "Return true is if connections pooling is enabled.", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-AsyncHttpClientConfig"));
        items.add(new ConfigurationItem("play.ws.ning.ioThreadMultiplier", "", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-AsyncHttpClientConfig"));
        items.add(new ConfigurationItem("play.ws.ning.maxConnectionsPerHost", " the maximum number of connections per host an AsyncHttpClient can handle.", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-AsyncHttpClientConfig"));
        items.add(new ConfigurationItem("play.ws.ning.maxConnectionsTotal", "Set the maximum number of connections an AsyncHttpClient can handle.", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-AsyncHttpClientConfig"));
        items.add(new ConfigurationItem("play.ws.ning.maxConnectionLifeTime", "", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-AsyncHttpClientConfig"));
        items.add(new ConfigurationItem("play.ws.ning.idleConnectionInPoolTimeout", "Set the maximum time an AsyncHttpClient will keep connection idle in pool.", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-AsyncHttpClientConfig"));
        items.add(new ConfigurationItem("play.ws.ning.webSocketIdleTimeout", "", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-AsyncHttpClientConfig"));
        items.add(new ConfigurationItem("play.ws.ning.maxNumberOfRedirects", "Set the maximum number of HTTP redirect", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-AsyncHttpClientConfig"));
        items.add(new ConfigurationItem("play.ws.ning.maxRequestRetry", "Set the number of time a request will be retried when an IOException occurs because of a Network exception.", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-AsyncHttpClientConfig"));
        items.add(new ConfigurationItem("play.ws.ning.disableUrlEncoding", "", "https://www.playframework.com/documentation/2.4.x/ScalaWS#Configuring-AsyncHttpClientConfig"));
        items.add(new ConfigurationItem("play.akka.config", "In case you want to use the akka.* settings for another Akka actor system, you can tell Play to load its Akka settings from another location. play.akka.config = \"my-akka\"", "https://www.playframework.com/documentation/2.4.x/ScalaAkka#Changing-configuration-prefix"));
        items.add(new ConfigurationItem("play.akka.actor-system", "By default the name of the Play actor system is application. You can change this via an entry in the conf/application.conf:\n"
                + "play.akka.actor-system = \"custom-name\"  \n Note: This feature is useful if you want to put your play application ActorSystem in an Akka cluster.", "https://www.playframework.com/documentation/2.4.x/ScalaAkka#Built-in-actor-system-name"));
        items.add(new ConfigurationItem("play.i18n.langs", "specify the languages supported by your application in the conf/application.conf file:\n"
                + "play.i18n.langs = [ \"en\", \"en-US\", \"fr\" ]", "https://www.playframework.com/documentation/2.4.x/ScalaI18N#Specifying-languages-supported-by-your-application"));
        items.add(new ConfigurationItem("play.http.requestHandler", "If you don’t want to place your request handler in the root package, or if you want to be able to configure different request handlers for different\n"
                + "environments, you can do this by configuring the play.http.requestHandler configuration property in application.conf:\n"
                + "play.http.requestHandler = \"com.example.RequestHandler\"", "https://www.playframework.com/documentation/2.4.x/ScalaHttpRequestHandlers#Configuring-the-http-request-handler"));
        items.add(new ConfigurationItem("play.modules.enabled", "To register this module with Play, append it’s fully qualified class name to the play.modules.enabled list in application.conf:\n"
                + "play.modules.enabled += \"modules.HelloModule\"", "https://www.playframework.com/documentation/2.4.x/ScalaDependencyInjection#Programmatic-bindings"));
        items.add(new ConfigurationItem("play.modules.disabled", "If there is a module that you don’t want to be loaded, you can exclude it by appending it to the play.modules.disabled property in\n"
                + "application.conf:\n"
                + "play.modules.disabled += \"play.api.db.evolutions.EvolutionsModule\"", "https://www.playframework.com/documentation/2.4.x/ScalaDependencyInjection#Excluding-modules"));
        items.add(new ConfigurationItem("play.application.loader", "When you override the ApplicationLoader\n"
                + "you need to tell Play. Add the following setting to your application.conf:\n"
                + "play.application.loader = \"modules.CustomApplicationLoader\"", "https://www.playframework.com/documentation/2.4.x/ScalaCompileTimeDependencyInjection#Application-entry-point"));
        items.add(new ConfigurationItem("jpa.default", "Finally you have to tell Play, which persistent unit should be used by your JPA provider. This is done by the jpa.default property in your\n"
                + "application.conf.\n"
                + "jpa.default=defaultPersistenceUnit", "https://www.playframework.com/documentation/2.4.x/JavaJPA#Creating-a-persistence-unit"));
        items.add(new ConfigurationItem("ebean.default", "The runtime library can be configured by putting the list of packages and/or classes that your Ebean models live in your application configuration\n"
                + "file. For example, if all your models are in the models package, add the following to conf/application.conf:\n"
                + "ebean.default = [\"models.*\"]", "https://www.playframework.com/documentation/2.4.x/JavaEbean#Configuring-the-runtime-library"));
        items.add(new ConfigurationItem("application.global", "By default, this object is defined in the root package, but you can define it wherever you want and then configure it in your application.conf\n"
                + "using application.global property.", "https://www.playframework.com/documentation/2.4.x/JavaGlobal#The-Global-object"));
        items.add(new ConfigurationItem("play.crypto.secret", "It is configured in application.conf, with the property name play.crypto.secret, and defaults to changeme. As the default suggests, it\n"
                + "should be changed for production. When started in prod mode, if Play finds that the secret is not set, or if it is set to changeme, Play will throw an error.", "https://www.playframework.com/documentation/2.4.x/ApplicationSecret#The-Application-Secret"));
        items.add(new ConfigurationItem("include", "Includes another conf file. Example: include \"development.conf\"", "https://www.playframework.com/documentation/2.4.x/ProductionConfiguration#Using--Dconfig.file"));
        items.add(new ConfigurationItem("play.filters.headers.frameOptions", "sets X-Frame-Options \"DENY\" by default. Any of the headers can be disabled by setting a configuration value of null, for example: play.filters.headers.frameOptions = null", "https://www.playframework.com/documentation/2.4.x/SecurityHeaders#Configuring-the-security-headers"));
        items.add(new ConfigurationItem("play.filters.headers.xssProtection", "sets X-XSS-Protection, “1; mode=block” by default. Any of the headers can be disabled by setting a configuration value of null, for example: play.filters.headers.frameOptions = null", "https://www.playframework.com/documentation/2.4.x/SecurityHeaders#Configuring-the-security-headers"));
        items.add(new ConfigurationItem("play.filters.headers.contentTypeOptions", "sets X-Content-Type-Options, “nosniff” by default. Any of the headers can be disabled by setting a configuration value of null, for example: play.filters.headers.frameOptions = null", "https://www.playframework.com/documentation/2.4.x/SecurityHeaders#Configuring-the-security-headers"));
        items.add(new ConfigurationItem("play.filters.headers.permittedCrossDomainPolicies", "sets X-Permitted-Cross-Domain-Policies, “master-only” by default. Any of the headers can be disabled by setting a configuration value of null, for example: play.filters.headers.frameOptions = null", "https://www.playframework.com/documentation/2.4.x/SecurityHeaders#Configuring-the-security-headers"));
        items.add(new ConfigurationItem("play.filters.headers.contentSecurityPolicy", "sets Content-Security-Policy, “default-src ‘self’” by default. Any of the headers can be disabled by setting a configuration value of null, for example: play.filters.headers.frameOptions = null", "https://www.playframework.com/documentation/2.4.x/SecurityHeaders#Configuring-the-security-headers"));
        items.add(new ConfigurationItem("play.filters.cors.pathPrefixes", "filter paths by a whitelist of path prefixes", "https://www.playframework.com/documentation/2.4.x/CorsFilter#Configuring-the-CORS-filter"));
        items.add(new ConfigurationItem("play.filters.cors.allowedOrigins", "allow only requests with origins from a whitelist (by default all origins are allowed)", "https://www.playframework.com/documentation/2.4.x/CorsFilter#Configuring-the-CORS-filter"));
        items.add(new ConfigurationItem("play.filters.cors.allowedHttpMethods", "allow only HTTP methods from a whitelist for preflight requests (by default all methods are allowed)", "https://www.playframework.com/documentation/2.4.x/CorsFilter#Configuring-the-CORS-filter"));
        items.add(new ConfigurationItem("play.filters.cors.allowedHttpHeaders", "allow only HTTP headers from a whitelist for preflight requests (by default all headers are allowed)", "https://www.playframework.com/documentation/2.4.x/CorsFilter#Configuring-the-CORS-filter"));
        items.add(new ConfigurationItem("play.filters.cors.exposedHeaders", "set custom HTTP headers to be exposed in the response (by default no headers are exposed)", "https://www.playframework.com/documentation/2.4.x/CorsFilter#Configuring-the-CORS-filter"));
        items.add(new ConfigurationItem("play.filters.cors.supportsCredentials", "disable/enable support for credentials (by default credentials support is enabled)", "https://www.playframework.com/documentation/2.4.x/CorsFilter#Configuring-the-CORS-filter"));
        items.add(new ConfigurationItem("play.filters.cors.preflightMaxAge", "set how long the results of a preflight request can be cached in a preflight result cache (by default 1 hour)", "https://www.playframework.com/documentation/2.4.x/CorsFilter#Configuring-the-CORS-filter"));
        items.add(new ConfigurationItem("play.ws.ssl.hostnameVerifierClass", "If you need to specify a different hostname verifier, you can configure application.conf to provide your own custom HostnameVerifier: play.ws.ssl.hostnameVerifierClass=org.example.MyHostnameVerifier", "https://www.playframework.com/documentation/2.4.x/HostnameVerification#Modifying-the-Hostname-Verifier"));
        items.add(new ConfigurationItem("logger.play.api.libs.ws.ssl", "To see the behavior of WS, you can configuring the SLF4J logger for debug output:\n"
                + "logger.play.api.libs.ws.ssl=DEBUG", "https://www.playframework.com/documentation/2.4.x/DebuggingSSL#Verbose-Debugging"));
        items.add(new ConfigurationItem("play.evolutions.enabled", "Evolutions are automatically activated if a database is configured in application.conf and evolution scripts are present. You can disable\n"
                + "them by setting play.evolutions.enabled=false.", "https://www.playframework.com/documentation/2.4.x/Evolutions#Evolutions-configuration"));
        items.add(new ConfigurationItem("play.evolutions.autocommit", "Whether autocommit should be used. If false, evolutions will be applied in a single transaction. Defaults to true.", "https://www.playframework.com/documentation/2.4.x/Evolutions#Evolutions-configuration"));
        items.add(new ConfigurationItem("play.evolutions.useLocks", "Whether a locks table should be used. This must be used if you have many Play nodes that may potentially run evolutions, but\n"
                + "you want to ensure that only one does. It will create a table called play_evolutions_lock, and use a SELECT FOR UPDATE NOWAIT or\n"
                + "SELECT FOR UPDATE to lock it. This will only work for Postgres, Oracle, and MySQL InnoDB. It will not work for other databases. Defaults to\n"
                + "false.", "https://www.playframework.com/documentation/2.4.x/Evolutions#Evolutions-configuration"));
        items.add(new ConfigurationItem("play.evolutions.autoApply", "Whether evolutions should be automatically applied. In dev mode, this will cause both ups and downs evolutions to be\n"
                + "automatically applied. In prod mode, it will cause only ups evolutions to be automatically applied. Defaults to false.", "https://www.playframework.com/documentation/2.4.x/Evolutions#Evolutions-configuration"));
        items.add(new ConfigurationItem("play.evolutions.autoApplyDowns", "Whether down evolutions should be automatically applied. In prod mode, this will cause down evolutions to be\n"
                + "automatically applied. Has no effect in dev mode. Defaults to false.", "https://www.playframework.com/documentation/2.4.x/Evolutions#Evolutions-configuration"));
        //Play Server Settings
        items.add(new ConfigurationItem("play.server.http.port", "# The HTTP port of the server. Use a value of \"disabled\" if the server shouldn't bind an HTTP port.", "https://www.playframework.com/documentation/2.4.x/ProductionConfiguration#Server-configuration-options"));
        items.add(new ConfigurationItem("play.server.http.address", "The interface address to bind to. i.e: address = \"0.0.0.0\"", "https://www.playframework.com/documentation/2.4.x/ProductionConfiguration#Server-configuration-options"));
        items.add(new ConfigurationItem("play.server.https.port", "THe HTTPS port of the server", "https://www.playframework.com/documentation/2.4.x/ProductionConfiguration#Server-configuration-options"));
        items.add(new ConfigurationItem("play.server.https.engineProvider", "The SSL engine provider. i.e: engineProvider = \"play.core.server.ssl.DefaultSSLEngineProvider\"", "https://www.playframework.com/documentation/2.4.x/ProductionConfiguration#Server-configuration-options"));
        items.add(new ConfigurationItem("play.server.https.keyStore.path", "The path to the keystore", "https://www.playframework.com/documentation/2.4.x/ProductionConfiguration#Server-configuration-options"));
        items.add(new ConfigurationItem("play.server.https.keyStore.type", "The type of the keystore.  i.e: type = \"JKS\"", "https://www.playframework.com/documentation/2.4.x/ProductionConfiguration#Server-configuration-options"));
        items.add(new ConfigurationItem("play.server.https.keyStore.password", "The password for the keystore", "https://www.playframework.com/documentation/2.4.x/ProductionConfiguration#Server-configuration-options"));
        items.add(new ConfigurationItem("play.server.https.keyStore.algorithm", "The algorithm to use. If not set, uses the platform default algorithm.", "https://www.playframework.com/documentation/2.4.x/ProductionConfiguration#Server-configuration-options"));
        items.add(new ConfigurationItem("play.server.https.trustStore.noCaVerification", "If true, does not do CA verification on client side certificates", "https://www.playframework.com/documentation/2.4.x/ProductionConfiguration#Server-configuration-options"));
        items.add(new ConfigurationItem("play.server.provider", "The type of ServerProvider that should be used to create the server. If not provided, the ServerStart class that instantiates the server will provide a default value.", "https://www.playframework.com/documentation/2.4.x/ProductionConfiguration#Server-configuration-options"));
        items.add(new ConfigurationItem("play.server.pidfile.path", "The path to the process id file created by the server when it runs. If set to \"/dev/null\" then no pid file will be created.", "https://www.playframework.com/documentation/2.4.x/ProductionConfiguration#Server-configuration-options"));
        items.add(new ConfigurationItem("play.server.netty.maxInitialLineLength", "The maximum length of the initial line. This effectively restricts the maximum length of a URL that the server will accept, the initial line consists of the method (3-7 characters), the URL, and the HTTP version (8 characters), including typical whitespace, the maximum URL length will be this number - 18.", "https://www.playframework.com/documentation/2.4.x/ProductionConfiguration#Server-configuration-options"));
        items.add(new ConfigurationItem("play.server.netty.maxHeaderSize", "The maximum length of the HTTP headers. The most common effect of this is a restriction in cookie length, including number of cookies and size of cookie values.", "https://www.playframework.com/documentation/2.4.x/ProductionConfiguration#Server-configuration-options"));
        items.add(new ConfigurationItem("play.server.netty.maxChunkSize", "# The maximum length of body bytes that Netty will read into memory at a time.\n"
                + "      # This is used in many ways.  Note that this setting has no relation to HTTP chunked transfer encoding - Netty will\n"
                + "      # read \"chunks\", that is, byte buffers worth of content at a time and pass it to Play, regardless of whether the body\n"
                + "      # is using HTTP chunked transfer encoding.  A single HTTP chunk could span multiple Netty chunks if it exceeds this.\n"
                + "      # A body that is not HTTP chunked will span multiple Netty chunks if it exceeds this or if no content length is\n"
                + "      # specified. This only controls the maximum length of the Netty chunk byte buffers.", "https://www.playframework.com/documentation/2.4.x/ProductionConfiguration#Server-configuration-options"));
        items.add(new ConfigurationItem("play.server.netty.log.wire", "Whether the Netty wire should be logged. Defaults to false", "https://www.playframework.com/documentation/2.4.x/ProductionConfiguration#Server-configuration-options"));

        return items;
    }
}
