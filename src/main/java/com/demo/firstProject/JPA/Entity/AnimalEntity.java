package com.demo.firstProject.JPA.Entity;

import com.demo.firstProject.Configuration.Domain;
import com.demo.firstProject.DTO.Animals.AnimalDTO;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "animals_tb")
public class AnimalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id")
    private long id;

    @Column(name = "animal_name", nullable = false, length = 30)
    private String name;

    @Column(name = "animal_image_profile", nullable = true)
    private String imageProfile;

    @Column(name = "animal_quantity", nullable = false)
    private long quantity;

    @Column(name = "animal_status_state", nullable = false)
    private boolean statusState = false;

    @Column(name = "animal_create_time", nullable = false)
    private LocalDateTime createTime;

//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "animal_category_fk")
    private AnimalCategoryEntity animalCategoryFK;

    @OneToMany(mappedBy = "animalFK", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AnimalIllustrationEntity> animalIllustration = new ArrayList<>();


    public AnimalDTO SetAnimal_dto() {
        AnimalDTO animaldto = new AnimalDTO();
        animaldto.setId(this.getId());
        animaldto.setName(this.getName());
        animaldto.setImageProfile(Domain.domainUrl + "/api/images/" + this.getImageProfile());
        animaldto.setQuantity(this.getQuantity());
        animaldto.setStatusState(this.isStatusState());
        animaldto.setCreateTime(this.getCreateTime());
        animaldto.setAnimalCategoryFK(this.getAnimalCategoryFK().getCategoryName());
        animaldto.setAnimalIllustration(this.getAnimalIllustration().stream().map(AnimalIllustrationEntity::SetAnimalIllustration_dto).collect(Collectors.toList()));
        return animaldto;
    }
}
