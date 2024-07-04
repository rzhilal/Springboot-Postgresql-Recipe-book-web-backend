package com.arutalalab.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", schema = "public")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Users {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "username", length = 255)
  private String username;

  @Column(name = "fullname", length = 255)
  private String fullname;

  @Column(name = "password")
  private String password;

  @Column(name = "role", length = 100)
  private String role;

  @Column(name = "is_deleted")
  private Boolean isDeleted;

  @Column(name = "created_by", length = 255)
  private String createdBy;

  @Column(name = "created_time")
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  private LocalDateTime createdTime;

  @Column(name = "modified_by", length = 255)
  private String modifiedBy;

  @Column(name = "modified_time")
  @Temporal(TemporalType.TIMESTAMP)
  @LastModifiedDate
  private LocalDateTime modifiedTime;
}
