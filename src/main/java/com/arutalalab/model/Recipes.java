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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity
@Table(name = "recipes")
@EntityListeners(AuditingEntityListener.class)
public class Recipes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Integer recipeId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = true)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = true)
    private Categories category;

    @ManyToOne
    @JoinColumn(name = "level_id", referencedColumnName = "level_id", nullable = true)
    private Levels level;

    @Column(name = "recipe_name", length = 255)
    private String recipeName;

    @Column(name = "image_filename", length = 255)
    private String imageFilename;

    @Column(name = "time_cook")
    private Integer timeCook;

    @Column(name = "ingredient", columnDefinition = "TEXT")
    private String ingredient;

    @Column(name = "how_to_cook", columnDefinition = "TEXT")
    private String howToCook;

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

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "time")
    private Integer time;
}
