package top.bioelectronic.sdk.robot.contact.user;

import lombok.Getter;

@Getter
public abstract class SNUserImpl implements SNUser {

    protected final SNUserProfile profile;
    protected final long id;


    public SNUserImpl(long id, SNUserProfile profile) {
        this.id = id;
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "[用户][" + getId() + "]";
    }
}
