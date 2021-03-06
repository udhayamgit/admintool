package de.chandre.admintool.log4j2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.OutputStreamAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.spi.StandardLevel;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.StringUtils;

/**
 * service for log4j2 manipulation
 * @author Andre
 * @since 1.0.0
 */
@Service("adminToolLog4j2Util")
public class AdminToolLog4j2Util 
{
	private static List<Level> LEVELS = new ArrayList<>(7);
	static {
		LEVELS.add(Level.OFF);
		LEVELS.add(Level.TRACE);
		LEVELS.add(Level.DEBUG);
		LEVELS.add(Level.INFO);
		LEVELS.add(Level.WARN);
		LEVELS.add(Level.ERROR);
		LEVELS.add(Level.FATAL);
	}
	
	private static final Comparator<Logger> LOGGER_COMP = new Comparator<Logger>() {
		@Override
		public int compare(Logger o1, Logger o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};
	
	private static final String DEFAULT_PATTERN = "%d{dd.MM.yyyy HH:mm:ss.SSS} %X{sessionId} [%t] %-5level %logger{36} : %msg%n";
	
	public static final String SESSION_APPENDER_NAME = "log4j2AppenderName";
	
	private Map<LoggerConfig, String> customLoggers = new ConcurrentReferenceHashMap<>();
	private Map<LoggerConfig, String> customParentLoggers = new ConcurrentReferenceHashMap<>();
	
	private Map<String, AdminToolLog4j2OutputStream> outputStreams = new ConcurrentReferenceHashMap<>();
	
	public int getCustomLoggerSize() {
		return customLoggers.size();
	}
	
	public int getCustomParentLoggerSize() {
		return customParentLoggers.size();
	}
	
	public boolean isCustom(String name) {
		return customLoggers.containsValue(name) || customParentLoggers.containsValue(name);
	}
	
	/**
	 * returns all parent loggers 
	 * @return
	 */
	public Collection<Logger> getParentLoggers() {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		List<Logger> loggers = new ArrayList<>(ctx.getLoggers());
		Map<String, Logger> parentMap = new HashMap<>();
		try {
			for (Logger logger : loggers) {
				if (null != logger.getParent() && parentMap.get(logger.getParent().getName()) == null) {
					parentMap.put(logger.getParent().getName(), logger.getParent());
				}
			}
			List<Logger> parents = new ArrayList<>(parentMap.values());
			Collections.sort(parents, LOGGER_COMP);
			return parents;
		} finally {
			loggers.clear();
			parentMap.clear();
		}
	}
	
	public Collection<String> getParentLoggerNames() {
		List<String> loggerNames = new ArrayList<>();
		for (Logger logger : getParentLoggers()) {
			loggerNames.add(logger.getName());
		}
		return loggerNames;
	}
	
	/**
	 * returns all loggers 
	 * @return
	 */
	public Collection<Logger> getLoggers() {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		List<Logger> loggers = new ArrayList<>(ctx.getLoggers());
		Collections.sort(loggers, LOGGER_COMP);
		return loggers;
	}
	
	/**
	 * returns all logger names including custom loggers
	 * 
	 * @since 1.1.1
	 * @return
	 */
	public Collection<String> getAllLoggerNames() {
		Set<String> loggerNames = new TreeSet<>();
		for (Logger logger : getParentLoggers()) {
			loggerNames.add(logger.getName());
		}
		for (Logger logger : getLoggers()) {
			loggerNames.add(logger.getName());
		}
		if (!customLoggers.isEmpty()) {
			for (Entry<LoggerConfig, String> entry : customLoggers.entrySet()) {
				loggerNames.add(entry.getKey().getName());
			}
		}
		if (!customParentLoggers.isEmpty()) {
			for (Entry<LoggerConfig, String> entry : customParentLoggers.entrySet()) {
				loggerNames.add(entry.getKey().getName());
			}
		}
		return loggerNames;
	}
	
	/**
	 * returns the a css class with optional prefix for the particular log level.<br>
	 * if prefix is set result will be &lt;prefix&gt;-&lt;css-class&gt;
	 * 
	 * @param prefix (optional) a prefic
	 * @param level the log level
	 * @return
	 */
	public String getLoggerLevelCss(String prefix, Level level) {
		if (null == prefix) {
			prefix = "";
		} else {
			prefix += "-";
		}
		if (level.intLevel() == StandardLevel.TRACE.intLevel()) {
			return prefix + "info";
		}
		if (level.intLevel() == StandardLevel.DEBUG.intLevel()) {
			return prefix + "primary";		
		}
		if (level.intLevel() == StandardLevel.INFO.intLevel()) {
			return prefix + "success";
		}
		if (level.intLevel() == StandardLevel.WARN.intLevel()) {
			return prefix + "warning";
		}
		if (level.intLevel() == StandardLevel.ERROR.intLevel()) {
			return prefix + "danger";
		}
		if (level.intLevel() == StandardLevel.FATAL.intLevel()) {
			return prefix + "muted";
		}
		if (level.intLevel() == StandardLevel.OFF.intLevel()) {
			return prefix + "muted";
		}
		return "";
	}
	
	/**
	 * returns fix amount of logger levels 
	 * @return
	 */
	public Collection<Level> getLevels() {
		return LEVELS;
	}
	
	private Level getLevel(final String levelStr) {
		Level level = Level.getLevel(levelStr);
		if (null == level || !LEVELS.contains(level)) {
			throw new IllegalArgumentException("wrong logger level: " + String.valueOf(levelStr));
		}
		return level;
	}
	
	/**
	 * changes the level of an logger
	 * 
	 * @param name logger name
	 * @param levelStr level as string
	 * @param parent if the logger is a parent logger
	 * @throws IllegalArgumentException
	 */
	public void changeLogger(final String name, final String levelStr, boolean parent) throws IllegalArgumentException
	{
		Level level = getLevel(levelStr);
		changeLogger(name, level, parent);
	}
	
	/**
	 * 
	 * @param name
	 * @param level
	 * @param parent
	 * @throws IllegalArgumentException
	 * @see {@link #changeLogger(String, String, boolean)}
	 */
	public void changeLogger(final String name, final Level level, boolean parent) throws IllegalArgumentException
	{
		if (null == name) {
			throw new IllegalArgumentException("logger name must not null");
		}
		String loggerName = name;
		if (name.equals("ROOT")) {
			loggerName = LogManager.ROOT_LOGGER_NAME;
		}
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(loggerName);
		if (null == loggerConfig) {
			throw new IllegalArgumentException("no logger config found for: " + String.valueOf(loggerName));
		}
		if (customLoggers.containsValue(loggerName)) {
			setLevelOnExistingCustomLogger(this.customLoggers, loggerName, level);
		}
		else if (customParentLoggers.containsValue(loggerName)) {
			setLevelOnExistingCustomLogger(this.customParentLoggers, loggerName, level);
		}
		else if (!loggerConfig.getName().equals(loggerName)) {
//			LoggerConfig loggerConfigNew = new LoggerConfig();
//			loggerConfigNew.setLevel(level);
//		    config.addLogger(loggerName, loggerConfigNew);
//		    if (parent) {
//		    	customParentLoggers.put(loggerConfigNew, loggerName);
//		    } else {
//		    	customLoggers.put(loggerConfigNew, loggerName);
//		    }
		    addCustomParentLogger(false, level, loggerName, Arrays.asList("Console"));
		}
		else {
			loggerConfig.setLevel(level);
		}
		ctx.updateLoggers();
	}
	
	private void setLevelOnExistingCustomLogger(Map<LoggerConfig, String> customLoggers, String loggerName, Level level) {
		for (Entry<LoggerConfig, String> entry : customLoggers.entrySet()) {
			if (entry.getValue().equals(loggerName)) {
				entry.getKey().setLevel(level);
			}
		}
	}
	
	public Collection<String> getAppenderNames() {
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();
		return config.getAppenders().keySet();
	}
	
	public String getAppendersForLogger(String loggerName) {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(loggerName);
		List<AppenderRef> appenderRefs = loggerConfig.getAppenderRefs();
		return StringUtils.collectionToCommaDelimitedString(appenderRefs.parallelStream().map(ar -> ar.getRef()).collect(Collectors.toSet()));
	}
	
	/**
	 * 
	 * @param additivity
	 * @param level
	 * @param loggerName
	 * @param appenderNames
	 * @param recursive
	 * 
	 * @since 1.1.6.4
	 */
	public void addCustomParentLogger(boolean additivity, String levelStr, String loggerName, Collection<String> appenderNames) {
		Level level = getLevel(levelStr);
		addCustomParentLogger(additivity, level, loggerName, appenderNames);
	}
	
	/**
	 * 
	 * @param additivity
	 * @param level
	 * @param loggerName
	 * @param appenderNames
	 * @param recursive
	 * 
	 * @since 1.1.6.4
	 */
	public void addCustomParentLogger(boolean additivity, Level level, String loggerName, Collection<String> appenderNames) {
		if (StringUtils.isEmpty(loggerName)) {
			throw new IllegalArgumentException("loggerName should not be null");
		}
		if (null == level) {
			throw new IllegalArgumentException("level should not be null");
		}
		
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		
		if (null == appenderNames) {
			appenderNames = Collections.emptyList();
		}
		List<AppenderRef> appenderRefs = appenderNames.stream()
				.map(name -> AppenderRef.createAppenderRef(name, null, null))
				.collect(Collectors.toList());
		
		LoggerConfig loggerConfigForTest = config.getLoggerConfig(loggerName);
		List<String> appenderRefsToAdd = new ArrayList<>(appenderNames);
		final LoggerConfig loggerConfig;
		if (null == loggerConfigForTest || StringUtils.isEmpty(loggerConfigForTest.getName()) || !loggerConfigForTest.getName().equals(loggerName)) {
			//create a new Logger
			loggerConfig = LoggerConfig.createLogger(additivity, level, loggerName, "true", appenderRefs.toArray(new AppenderRef[]{}), null, config, null);
			customParentLoggers.put(loggerConfig, loggerName);
		}
		else {
			//manage a existing logger
			loggerConfig = config.getLoggerConfig(loggerName);
			//remove appenderRef which are not selected anymore
			List<AppenderRef> currentRefs = loggerConfig.getAppenderRefs();
			Iterator<AppenderRef> refIter = currentRefs.iterator();
			while (refIter.hasNext()) {
				AppenderRef ref = (AppenderRef) refIter.next(); 
				if (!appenderNames.contains(ref.getRef())) {
					refIter.remove();
					loggerConfig.removeAppender(ref.getRef());
				} else {
					appenderRefsToAdd.remove(ref.getRef());
				}
				
			}
			//add appendersRefs
			if (!CollectionUtils.isEmpty(appenderRefsToAdd)) {
				appenderRefsToAdd.forEach(appenderRefName -> {
					loggerConfig.getAppenderRefs().add(AppenderRef.createAppenderRef(appenderRefName, null, null));
				});
			}
		}
		 
		config.getAppenders().entrySet().stream()
				.filter(entry -> appenderRefsToAdd.contains(entry.getKey()))
				.forEach(appenderEntry -> loggerConfig.addAppender(appenderEntry.getValue(), null, null));
		
		config.addLogger(loggerName, loggerConfig);
		ctx.updateLoggers();
	}
	
	/**
	 * removes all custom loggers (without parents)
	 * 
	 * @throws IllegalArgumentException
	 */
	public void removeCustomLoggers() throws IllegalArgumentException
	{
		removeCustomLoggers(customLoggers);
	}
	
	/**
	 * removes all custom parent loggers
	 * @throws IllegalArgumentException
	 */
	public void removeCustomParentLoggers() throws IllegalArgumentException
	{
		removeCustomLoggers(customParentLoggers);
	}
	
	public void removeCustomLoggers(Map<LoggerConfig, String> customMap) throws IllegalArgumentException
	{
		if (customMap.isEmpty()) {
			return;
		}
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		for (Entry<LoggerConfig, String> entry : customMap.entrySet()) {
			config.removeLogger(entry.getValue());
		}
		ctx.updateLoggers();
		customMap.clear();
	}
	
	
	/**
	 * returns the default log message pattern (used in template)
	 * @return
	 * @since 1.1.1
	 */
	public String getDefaultPattern() {
		return DEFAULT_PATTERN;
	}
	
	/**
	 * creates the custom output steam appender and returns the name
	 * 
	 * @param name
	 * @param pattern
	 * @param encoding
	 * @param loggerNames
	 * @param levelStr
	 * @return
	 * @since 1.1.1
	 */
	public String createOutputStreamAppender(String name, String pattern, String encoding, Collection<String> loggerNames, 
			String levelStr, boolean recursive, boolean overrideLogLevel) {
		Level level = getLevel(levelStr);
		String encodingToUse = StringUtils.isEmpty(encoding) ? "UTF-8" : encoding;
		PatternLayout layout = PatternLayout.newBuilder()
				.withPattern(StringUtils.isEmpty(pattern) ? DEFAULT_PATTERN : pattern)
				.withCharset(Charset.forName(encodingToUse))
				.build();
		
		String appenderName = StringUtils.isEmpty(name) ? UUID.randomUUID().toString() : name;
		
		AdminToolLog4j2OutputStream baos = new AdminToolLog4j2OutputStream(4096, encodingToUse);
		outputStreams.put(appenderName, baos);
		
		OutputStreamAppender appender = OutputStreamAppender.newBuilder()
				.setName(appenderName)
				.setTarget(baos)
				.setLayout(layout)
				.setFollow(false)
				.build();
		
		appender.start();
		
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();
		config.addAppender(appender);
		
		Collection<String> parentLoggerNames = getParentLoggerNames();
		
		Map<String, LoggerConfig> configs = getRecursiveLoggerConfigs(loggerNames, recursive, config);
		configs.entrySet().forEach(configEntry ->{
			configEntry.getValue().addAppender(appender, level, null);
			if (overrideLogLevel) {
				baos.addOriginalLevel(configEntry.getKey(), configEntry.getValue().getLevel());
				changeLogger(configEntry.getKey(), level, parentLoggerNames.contains(configEntry.getKey()));
			}
		});
		
		ctx.updateLoggers();
		return appenderName;
	}

	/**
	 * 
	 * @param loggerNames
	 * @param recursive
	 * @param config
	 * @return
	 */
	private Map<String, LoggerConfig> getRecursiveLoggerConfigs(Collection<String> loggerNames, boolean recursive,
			final Configuration config) {
		Map<String, LoggerConfig> configs = new HashMap<>();
		for (String configuredLoggerName : getAllLoggerNames()) {
			for (String loggerNameToApply : loggerNames) {
				
				boolean apply = (recursive && configuredLoggerName.startsWith(loggerNameToApply))
						|| (!recursive && configuredLoggerName.equalsIgnoreCase(loggerNameToApply));
				if (apply) {
					configs.put(configuredLoggerName, config.getLoggerConfig(configuredLoggerName));
				}
			}
		}
		return configs;
	}
	
	/**
	 * returns the log messages from custom appenders output stream
	 * 
	 * @param appenderName
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 * @since 1.1.1
	 */
	public String getStringOutput(String appenderName, String encoding) throws UnsupportedEncodingException {
		AdminToolLog4j2OutputStream baos = outputStreams.get(appenderName);
		String output = "";
		if (null != baos) {
			output = baos.getAndReset(encoding);
		}
		return output.trim().isEmpty() ? null : output;
		
	}
	
	/**
	 * closes output stream and removes appender from loggers
	 * @param appenderName
	 * @throws IOException
	 * @since 1.1.1
	 */
	public void closeOutputStreamAppender(String appenderName) throws IOException {
		if (null == appenderName) {
			return;
		}
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();
		AdminToolLog4j2OutputStream baos = outputStreams.get(appenderName);
		
		if (null != config && null != config.getAppenders()) {
			OutputStreamAppender appender = config.getAppender(appenderName);
			if (null != appender) {
				appender.stop();
				
				Collection<String> parentLoggerNames = getParentLoggerNames();
				
				for (String configuredLoggerName : getAllLoggerNames()) {
					LoggerConfig loggerConfig = config.getLoggerConfig(configuredLoggerName);
					loggerConfig.removeAppender(appender.getName());
					if (null != baos.getOriginalLevel(configuredLoggerName)) {
						changeLogger(configuredLoggerName, baos.getOriginalLevel(configuredLoggerName), 
								parentLoggerNames.contains(configuredLoggerName));
					}
				}
				//unsure about, if removing the appender from logger config if it gets also removed from logger instance too...
				removeAppender(appender, getParentLoggers());
				removeAppender(appender, getLoggers());
				appender.getManager().getByteBuffer().clear();
				
				ctx.updateLoggers();
			}
		}
		
		if (null != baos) {
			try {
				baos.close();
				baos.clearOriginalLevels();
			} catch (Exception ignore) {
			} finally {
				outputStreams.remove(appenderName);
			}
		}
	}
	
	private void removeAppender(Appender appender, Collection<Logger> appenders) {
		for (Logger logger : appenders) {
			logger.removeAppender(appender);
		}
	}
	
	public Map<String, Appender> getAppenders() {
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();
		
		return config.getAppenders();
	}
	
}
