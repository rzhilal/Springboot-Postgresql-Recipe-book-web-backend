package com.arutalalab.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.arutalalab.model.id.FavoriteFoodsId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "favorite_foods")
@EntityListeners(AuditingEntityListener.class)
@IdClass(FavoriteFoodsId.class)
public class FavoriteFoods implements Serializable {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_favorite_relations_users"))
    private Users user;

    @Id
    @Column(name = "recipe_id")
    private Integer recipeId;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_favorite_relations_recipes"))
    private Recipes recipe;

    @Column(name = "is_favorite")
    private Boolean isFavorite;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "created_time")
    @CreatedDate
    private LocalDateTime createdTime;

    @Column(name = "modified_by", length = 255)
    private String modifiedBy;

    @Column(name = "modified_time")
    @LastModifiedDate
    private LocalDateTime modifiedTime;
}
