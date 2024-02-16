package com.remote.submission.model;

import jakarta.persistence.*;
import lombok.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Lob
    @Column(name="code_content")
    private byte[] codeContent;
    private String language;
    private long userId;
}
