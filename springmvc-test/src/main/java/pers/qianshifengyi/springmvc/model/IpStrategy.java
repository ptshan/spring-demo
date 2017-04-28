package pers.qianshifengyi.springmvc.model;

import java.util.Date;

public class IpStrategy {
    private String id;

    private String ip;

    private Integer ipType;

    private String fromIp;

    private String toIp;

    private String forbiddenReason;

    private Integer forbiddenType;

    private Date forbiddenStartTime;

    private Date forbiddenEndTime;

    private Integer isEnabled;

    private Date createTime;

    private String createBy;

    private String enabledBy;

    private String disabledBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Integer getIpType() {
        return ipType;
    }

    public void setIpType(Integer ipType) {
        this.ipType = ipType;
    }

    public String getFromIp() {
        return fromIp;
    }

    public void setFromIp(String fromIp) {
        this.fromIp = fromIp == null ? null : fromIp.trim();
    }

    public String getToIp() {
        return toIp;
    }

    public void setToIp(String toIp) {
        this.toIp = toIp == null ? null : toIp.trim();
    }

    public String getForbiddenReason() {
        return forbiddenReason;
    }

    public void setForbiddenReason(String forbiddenReason) {
        this.forbiddenReason = forbiddenReason == null ? null : forbiddenReason.trim();
    }

    public Integer getForbiddenType() {
        return forbiddenType;
    }

    public void setForbiddenType(Integer forbiddenType) {
        this.forbiddenType = forbiddenType;
    }

    public Date getForbiddenStartTime() {
        return forbiddenStartTime;
    }

    public void setForbiddenStartTime(Date forbiddenStartTime) {
        this.forbiddenStartTime = forbiddenStartTime;
    }

    public Date getForbiddenEndTime() {
        return forbiddenEndTime;
    }

    public void setForbiddenEndTime(Date forbiddenEndTime) {
        this.forbiddenEndTime = forbiddenEndTime;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getEnabledBy() {
        return enabledBy;
    }

    public void setEnabledBy(String enabledBy) {
        this.enabledBy = enabledBy == null ? null : enabledBy.trim();
    }

    public String getDisabledBy() {
        return disabledBy;
    }

    public void setDisabledBy(String disabledBy) {
        this.disabledBy = disabledBy == null ? null : disabledBy.trim();
    }
}