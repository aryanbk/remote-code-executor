package com.remote.submission.repository;

import com.remote.submission.model.FileSubmission;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileSubmission, Long> {

    @NonNull
    Optional<FileSubmission> findById(long id);

}
