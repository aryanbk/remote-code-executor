package com.remote.consumer.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "code_execution_result")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CodeExecutionResult {
    @Id
    private String id;
    private String sessionId;
    @Lob
    @Column(name = "result")
    private byte[] result;
    @Lob
    @Column(name = "error")
    private byte[] error;
    private String language;
    private int userId;

    public CodeExecutionResult(CodeSubmission codeSubmission, byte[] result) {
        this.id = codeSubmission.getId();
        this.sessionId = codeSubmission.getSessionId();
        this.result = result;
        this.error = new byte[0];
        this.language = codeSubmission.getLanguage();
        this.userId = codeSubmission.getUserId();
    }

    public CodeExecutionResult(CodeSubmission codeSubmission, byte[] result, byte[] error) {
        this.id = codeSubmission.getId();
        this.sessionId = codeSubmission.getSessionId();
        this.result = result;
        this.error = error;
        this.language = codeSubmission.getLanguage();
        this.userId = codeSubmission.getUserId();
    }
}
