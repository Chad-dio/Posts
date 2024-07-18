package cn.chad.post.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChannelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    private Boolean isDefault;

    private Boolean status;

    private Integer ord;
}
