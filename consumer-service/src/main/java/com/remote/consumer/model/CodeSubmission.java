package com.remote.consumer.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "code_submission")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CodeSubmission {
    @Id
    private String id;
    private String sessionId;
    @Lob
    @Column(name="code_content")
    private byte[] codeContent;
    private String language;
    private int userId;
}
