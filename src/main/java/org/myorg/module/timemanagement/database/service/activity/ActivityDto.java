package org.myorg.module.timemanagement.database.service.activity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import org.myorg.module.timemanagement.database.domainobject.DbActivity;
import org.myorg.modules.modules.dto.AbstractDto;

import java.util.Date;

@JsonTypeName("activity")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = { "id" })
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto implements AbstractDto {

    private long id;

    private String name;

    private Date creationDatetime;

    private Long userId;

    public static ActivityDto from(DbActivity dbActivity) {
        if (dbActivity == null) {
            return null;
        }

        return ActivityDto.builder()
                .id(dbActivity.getId())
                .name(dbActivity.getName())
                .creationDatetime(dbActivity.getCreationDatetime())
                .userId(dbActivity.getUser() != null ? dbActivity.getUser().getId() : null)
                .build();
    }
}
