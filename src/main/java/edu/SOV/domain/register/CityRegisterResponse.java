package edu.SOV.domain.register;

public class CityRegisterResponse {
    private boolean exist;
    private Boolean perm;

    public CityRegisterResponse(boolean exist, Boolean perm) {
        this.exist = exist;
        this.perm = perm;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public Boolean getPerm() {
        return perm;
    }

    public void setPerm(Boolean perm) {
        this.perm = perm;
    }
}
