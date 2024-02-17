package com.remote.consumer.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "file_submission")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FileSubmission {
    @Id
    @JsonProperty("id")
    private long id;
    @Lob
    @Column(name="code_content")
    @JsonProperty("codeContent")
    private byte[] codeContent;
    @JsonProperty("language")
    private String language;
    @JsonProperty("userId")
    private long userId;
}