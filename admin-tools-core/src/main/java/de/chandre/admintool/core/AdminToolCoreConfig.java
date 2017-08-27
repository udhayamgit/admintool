package de.chandre.admintool.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * configuration component for admintool
 * @author Andre
 *
 */
@Component("adminToolConfig")
public class AdminToolCoreConfig implements AdminToolConfig
{
	private static final Log LOGGER = LogFactory.getLog(AdminToolCoreConfig.class);
	
	@Value("${admintool.core.enabled:true}")
	private boolean enabled;
	
	@Value("${admintool.core.useCDN:true}")
	private boolean useCDNs;
	
	@Value("${admintool.core.adminLTE.cdn.version:2.3.11}")
	private String adminLTECdnVersion;
	
	@Value("${admintool.core.adminlte.jquery.path:plugins/jQuery/jquery-2.2.3.min.js}")
	private String adminLTEjqueryPath;
	
	@Value("${admintool.core.jquery.path:}")
	private String jqueryPath;
	
	@Value("${admintool.core.fontAwsome.cdn.version:4.7.0}")
	private String fontAwsomeCdnVersion;
	
	@Value("${admintool.core.fontAwsome.cdn.useBower:true}")
	private boolean fontAwsomeCdnUseBower;
	
	@Value("${admintool.core.ionicons.cdn.version:2.0.1}")
	private String ionIconsCdnVersion;
	
	@Value("${admintool.core.ionicons.cdn.useBower:true}")
	private boolean ionIconsCdnUseBower;
	
	@Value("${admintool.core.showStacktraceOnErrorPage:true}")
	private boolean showStacktraceOnErrorPage;
	
	@Value("${admintool.core.stripRootContext:}")
	private String stripRootContext;
	
	@Value("${admintool.core.internationalizationEnabled:false}")
	private boolean internationalizationEnabled;
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/** 
	 * if set to false webJars included in in packaging will be used. useful for workplaces with not Internet access
	 * @return the useCDNs
	 */
	public boolean isUseCDNs() {
		return useCDNs;
	}

	/**
	 * if set to false webJars included in in packaging will be used. useful for workplaces with not Internet access
	 * @param useCDNs the useCDNs to set
	 */
	public void setUseCDNs(boolean useCDNs) {
		this.useCDNs = useCDNs;
	}

	/**
	 * the version to use of adminLTE in CDN of http://www.webjars.org/
	 * @return the adminLTECdnVersion
	 */
	public String getAdminLTECdnVersion() {
		return adminLTECdnVersion;
	}

	/**
	 * the version to use of adminLTE in CDN of http://www.webjars.org/
	 * @param adminLTECdnVersion the adminLTECdnVersion to set
	 */
	public void setAdminLTECdnVersion(String adminLTECdnVersion) {
		this.adminLTECdnVersion = adminLTECdnVersion;
	}
	
	/**
	 * Path (relative to own project or URL to CDN) for jQuery. 
	 * Setting the jqueryPath is only required if using a different AdminLTE version than the configured one
	 * and the (AdminLTE's) distributed version of jQuery or name has been changed
	 * @return
	 * @since 1.0.2
	 */
	public String getJqueryPath() {
		return jqueryPath;
	}

	/**
	 * Path (relative to own project or URL to CDN) for jQuery. 
	 * Setting the jqueryPath is only required if using a different AdminLTE version than the configured one
	 * and the (AdminLTE's) distributed version of jQuery or name has been changed
	 * @param jqueryPath
	 * @since 1.0.2
	 */
	public void setJqueryPath(String jqueryPath) {
		this.jqueryPath = jqueryPath;
	}

	/**
	 * relative path to jquery within adminLTE package
	 * @return
	 * @since 1.1.4
	 */
	public String getAdminLTEjqueryPath() {
		return adminLTEjqueryPath;
	}

	/**
	 * relative path to jquery within adminLTE package
	 * @param adminLTEjqueryPath
	 * @since 1.1.4
	 */
	public void setAdminLTEjqueryPath(String adminLTEjqueryPath) {
		this.adminLTEjqueryPath = adminLTEjqueryPath;
	}

	/**
	 * the version to use of fontAwsome in CDN of http://www.webjars.org/
	 * @since 1.0.1
	 * @return the fontAwsomeCdnVersion
	 */
	public String getFontAwsomeCdnVersion() {
		return fontAwsomeCdnVersion;
	}

	/**
	 * the version to use of fontAwsome in CDN of http://www.webjars.org/
	 * @since 1.0.1
	 * @param fontAwsomeCdnVersion the fontAwsomeCdnVersion to set
	 */
	public void setFontAwsomeCdnVersion(String fontAwsomeCdnVersion) {
		this.fontAwsomeCdnVersion = fontAwsomeCdnVersion;
	}

	/**
	 * if {@link AdminToolConfig#WEBJARS_CDN_PREFIX_BOWER} should be used or {@link AdminToolConfig#WEBJARS_CDN_PREFIX}
	 * @since 1.1.4
	 * @return
	 */
	public boolean isFontAwsomeCdnUseBower() {
		return fontAwsomeCdnUseBower;
	}

	/**
	 * @since 1.1.4
	 * @return
	 */
	public String getIonIconsCdnVersion() {
		return ionIconsCdnVersion;
	}

	/**
	 * @since 1.1.4
	 * @param ionIconsCdnVersion
	 */
	public void setIonIconsCdnVersion(String ionIconsCdnVersion) {
		this.ionIconsCdnVersion = ionIconsCdnVersion;
	}

	/**
	 * @since 1.1.4
	 * @return
	 */
	public boolean isIonIconsCdnUseBower() {
		return ionIconsCdnUseBower;
	}

	/**
	 * @since 1.1.4
	 * @param ionIconsCdnUseBower
	 */
	public void setIonIconsCdnUseBower(boolean ionIconsCdnUseBower) {
		this.ionIconsCdnUseBower = ionIconsCdnUseBower;
	}

	/**
	 * if {@link AdminToolConfig#WEBJARS_CDN_PREFIX_BOWER} should be used or {@link AdminToolConfig#WEBJARS_CDN_PREFIX}
	 * @since 1.1.4
	 * @param fontAwsomeCdnUseBower
	 */
	public void setFontAwsomeCdnUseBower(boolean fontAwsomeCdnUseBower) {
		this.fontAwsomeCdnUseBower = fontAwsomeCdnUseBower;
	}

	/**
	 * @return the showStacktraceOnErrorPage
	 */
	public boolean isShowStacktraceOnErrorPage() {
		return showStacktraceOnErrorPage;
	}

	/**
	 * @param showStacktraceOnErrorPage the showStacktraceOnErrorPage to set
	 */
	public void setShowStacktraceOnErrorPage(boolean showStacktraceOnErrorPage) {
		this.showStacktraceOnErrorPage = showStacktraceOnErrorPage;
	}
	
	/**
	 * a string value which is being removed from root context URI
	 * @since 1.0.2
	 * @return
	 */
	public String getStripRootContext() {
		return stripRootContext;
	}

	/**
	 * a string value which is being removed from root context URI
	 * @since 1.0.2
	 * @param stripRootContext
	 */
	public void setStripRootContext(String stripRootContext) {
		this.stripRootContext = stripRootContext;
	}
	
	/**
	 * 
	 * @since 1.1.6
	 * @return
	 */
	public boolean isInternationalizationEnabled() {
		return internationalizationEnabled;
	}

	/**
	 * @since 1.1.6
	 * @param enableLocalization
	 */
	public void setInternationalizationEnabled(boolean enableLocalization) {
		this.internationalizationEnabled = enableLocalization;
	}

	@Override
	public void printConfig() {
		LOGGER.debug(toString());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdminToolCoreConfig [enabled=").append(enabled).append(", useCDNs=").append(useCDNs)
				.append(", adminLTECdnVersion=").append(adminLTECdnVersion).append(", jqueryPath=").append(jqueryPath)
				.append(", adminLTEjqueryPath=").append(adminLTEjqueryPath).append(", fontAwsomeCdnVersion=")
				.append(fontAwsomeCdnVersion).append(", fontAwsomeCdnUseBower=").append(fontAwsomeCdnUseBower)
				.append(", ionIconsCdnVersion=").append(ionIconsCdnVersion).append(", ionIconsCdnUseBower=")
				.append(ionIconsCdnUseBower).append(", showStacktraceOnErrorPage=").append(showStacktraceOnErrorPage)
				.append(", stripRootContext=").append(stripRootContext)
				.append(", internationalizationEnabled=").append(internationalizationEnabled).append("]");
		return builder.toString();
	}
}
