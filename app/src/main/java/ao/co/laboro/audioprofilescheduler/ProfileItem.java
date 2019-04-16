package ao.co.laboro.audioprofilescheduler;

import java.io.Serializable;

public class ProfileItem implements Serializable {
    private String time;
    private String part;
    private String status;
    private Boolean ativo;
    private String desc;
    private Boolean repeat;
    private String days_to_repeat;
    private int id;

    public ProfileItem(String time, String part, String status, Boolean ativo, String desc, Boolean repeat, String days_to_repeat) {
        this.time = time;
        this.part = part;
        this.status = status;
        this.ativo = ativo;
        this.desc = desc;
        this.repeat = repeat;
        this.days_to_repeat = days_to_repeat;
    }

    public String getTime() {
        return time;
    }

    public String getPart() {
        return part;
    }

    public String getStatus() {
        return status;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public String getDesc() {
        return desc;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public String getDays_to_repeat() {
        return days_to_repeat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    public void setDays_to_repeat(String days_to_repeat) {
        this.days_to_repeat = days_to_repeat;
    }
}
