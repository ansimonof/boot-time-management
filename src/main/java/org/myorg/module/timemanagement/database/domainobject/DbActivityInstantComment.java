package org.myorg.module.timemanagement.database.domainobject;

import lombok.*;
import org.myorg.module.timemanagement.TimeManagementModuleConsts;
import org.myorg.modules.modules.database.DomainObject;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = TimeManagementModuleConsts.DB_PREFIX + "comment"
)
@NamedQueries(
        value = {
                @NamedQuery(
                        name = DbActivityInstantComment.QUERY_FIND_ALL_BY_FK_ACTIVITY_INSTANT,
                        query = "select c from DbActivityInstantComment c where c.activityInstant.id = :"
                                + DbActivityInstantComment.FIELD_FK_ACTIVITY_INSTANT
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DbActivityInstantComment extends DomainObject {

    public static final String FIELD_TEXT = "text";
    public static final String FIELD_CREATION_DATETIME = "creation_datetime";
    public static final String FIELD_FK_ACTIVITY_INSTANT = "fk_activity_instant";

    public static final String QUERY_FIND_ALL_BY_FK_ACTIVITY_INSTANT = "DbActivityInstantComment.findAllByFkActivityInstant";

    @Column(name = FIELD_TEXT, nullable = false)
    private String text;

    @Column(name = FIELD_CREATION_DATETIME, nullable = false)
    @Setter(AccessLevel.PRIVATE)
    private Date creationDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = FIELD_FK_ACTIVITY_INSTANT, nullable = false)
    private DbActivityInstant activityInstant;

    @PrePersist
    private void prePersist() {
        creationDatetime = new Date();
    }
}
