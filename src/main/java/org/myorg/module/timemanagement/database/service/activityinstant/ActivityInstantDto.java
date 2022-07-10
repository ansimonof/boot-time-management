package org.myorg.module.timemanagement.database.service.activityinstant;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import org.myorg.module.timemanagement.database.domainobject.DbActivityInstant;
import org.myorg.modules.modules.dto.AbstractDto;

import java.util.Date;

@JsonTypeName("activity_instant")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = { "id" })
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityInstantDto implements AbstractDto {

    private long id;

    private Date startDatetime;

    private Date endDatetime;

    private Long activityId;

    public static ActivityInstantDto from(DbActivityInstant dbActivityInstant) {
        if (dbActivityInstant == null) {
            return null;
        }

        return ActivityInstantDto.builder()
                .id(dbActivityInstant.getId())
                .startDatetime(dbActivityInstant.getStartDatetime())
                .endDatetime(dbActivityInstant.getEndDatetime())
                .activityId(dbActivityInstant.getActivity() != null ? dbActivityInstant.getActivity().getId() : null)
                .build();
    }
}
