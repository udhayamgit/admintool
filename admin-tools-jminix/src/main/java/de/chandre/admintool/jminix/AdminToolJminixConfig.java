package de.chandre.admintool.jminix;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import de.chandre.admintool.core.AdminToolConfig;

@Component("adminToolJminixConfig")
public class AdminToolJminixConfig implements AdminToolConfig {
	
	private static final Log LOGGER = LogFactory.getLog(AdminToolJminixConfig.class);
	
	@Value("${adminTool.jmx.enabled:true}")
	private boolean enabled;
	
	@Value("#{'${admintool.jmx.securityRoles:}'.split(';')}")
	private Set<String> securityRoles = new HashSet<>();
	
	@Value("${admintool.jmx.componentPosition:}")
	private Integer componentPosition;
	
	@Value("${admintool.jmx.mustacheVersion:2.3.0}")
	private String mustacheVersion;
	
	@Value("${adminTool.jmx.updateAllowed:true}")
	private boolean updateAllowed;

	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<String> getSecurityRoles() {
		return securityRoles;
	}

	public Integer getComponentPosition() {
		return componentPosition;
	}

	public void setComponentPosition(Integer componentPosition) {
		this.componentPosition = componentPosition;
	}
	
	public String getMustacheVersion() {
		return mustacheVersion;
	}

	public void setMustacheVersion(String mustacheVersion) {
		this.mustacheVersion = mustacheVersion;
	}

	public boolean isUpdateAllowed() {
		return updateAllowed;
	}

	@Override
	public void printConfig() {
		LOGGER.debug(toString());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdminToolJminixConfig [enabled=").append(enabled).append(", securityRoles=")
				.append(securityRoles).append(", componentPosition=").append(componentPosition)
				.append(", mustacheVersion=").append(mustacheVersion).append(", updateAllowed=").append(updateAllowed)
				.append("]");
		return builder.toString();
	}

	
}
