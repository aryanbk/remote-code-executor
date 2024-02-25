package com.remote.submission.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remote.submission.model.CodeSubmission;

public interface CodeSubmissionRepository extends JpaRepository<CodeSubmission, String> {
    Optional<CodeSubmission> findById(String id);
}
