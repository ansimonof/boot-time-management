package org.myorg.module.timemanagement.database.domainobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myorg.module.timemanagement.TimeManagementModuleConsts;
import org.myorg.modules.modules.database.DomainObject;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = TimeManagementModuleConsts.DB_PREFIX + "activity_instant"
)
@NamedQueries(
        value = {
                @NamedQuery(
                        name = DbActivityInstant.QUERY_FIND_ALL_BY_FK_ACTIVITY,
                        query = "select ai from DbActivityInstant ai where ai.activity.id = :"
                                + DbActivityInstant.FIELD_FK_ACTIVITY
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DbActivityInstant extends DomainObject {

    public static final String FIELD_START_DATETIME = "start_datetime";
    public static final String FIELD_END_DATETIME = "end_datetime";
    public static final String FIELD_FK_ACTIVITY = "fk_activity";

    public static final String QUERY_FIND_ALL_BY_FK_ACTIVITY = "DbActivityInstant.findAllByFkActivity";

    @Column(name = FIELD_START_DATETIME, nullable = false)
    private Date startDatetime;

    @Column(name = FIELD_END_DATETIME)
    private Date endDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = FIELD_FK_ACTIVITY, nullable = false)
    private DbActivity activity;
}
