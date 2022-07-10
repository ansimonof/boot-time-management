package org.myorg.module.timemanagement.database.service.activityinstantcomment;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import org.myorg.module.timemanagement.database.domainobject.DbActivityInstantComment;
import org.myorg.modules.modules.dto.AbstractDto;

import java.util.Date;

@JsonTypeName("activity_instant_comment")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = { "id" })
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityInstantCommentDto implements AbstractDto {

    private long id;

    private String text;

    private Date creationDatetime;

    private Long activityInstantId;

    public static ActivityInstantCommentDto from(DbActivityInstantComment domainObject) {
        if (domainObject == null) {
            return null;
        }

        return ActivityInstantCommentDto.builder()
                .id(domainObject.getId())
                .text(domainObject.getText())
                .activityInstantId(domainObject.getActivityInstant() != null
                        ? domainObject.getActivityInstant().getId() : null)
                .creationDatetime(domainObject.getCreationDatetime())
                .build();
    }
}
