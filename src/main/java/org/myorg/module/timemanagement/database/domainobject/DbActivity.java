package org.myorg.module.timemanagement.database.domainobject;

import lombok.*;
import org.myorg.module.core.database.domainobject.DbUser;
import org.myorg.module.timemanagement.TimeManagementModuleConsts;
import org.myorg.modules.modules.database.DomainObject;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = TimeManagementModuleConsts.DB_PREFIX + "activity",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = { DbActivity.FIELD_NAME, DbActivity.FIELD_FK_USER }
                )
        }
)
@NamedQueries(
        value = {
                @NamedQuery(
                        name = DbActivity.QUERY_FIND_ALL_BY_FK_USER,
                        query = "select a from DbActivity a where a.user.id = :" + DbActivity.FIELD_FK_USER
                ),
                @NamedQuery(
                        name = DbActivity.QUERY_FIND_BY_NAME_AND_FK_USER,
                        query = "select a from DbActivity a where a.user.id = :" + DbActivity.FIELD_FK_USER
                                + " and a.name = :" + DbActivity.FIELD_NAME
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DbActivity extends DomainObject {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_CREATION_DATETIME = "creation_datetime";
    public static final String FIELD_FK_USER = "fk_user";

    public static final String QUERY_FIND_ALL_BY_FK_USER = "DbActivity.findAllByFkUser";
    public static final String QUERY_FIND_BY_NAME_AND_FK_USER = "DbActivity.findByNameAndFkUser";

    @Column(name = FIELD_NAME, nullable = false)
    private String name;

    @Column(name = FIELD_CREATION_DATETIME, nullable = false)
    @Setter(value = AccessLevel.NONE)
    private Date creationDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = FIELD_FK_USER, nullable = false)
    private DbUser user;

    @PrePersist
    private void prePersist() {
        creationDatetime = new Date();
    }

}
