package com.service.backend.JPA.Entity.Animal;

import com.service.backend.DTO.Animals.AnimalDTO;
import com.service.backend.JPA.Entity.Account.AccountEntity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity(name = "animal_tb")
public class AnimalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id")
    private Long id;

    @Column(name = "animal_name", nullable = false, length = 30)
    private String name;

    @Column(name = "animal_image_profile", nullable = false)
    private String imageProfile;

    @Column(name = "animal_quantity", nullable = false)
    private int quantity;

    @Column(name = "animal_status_state", nullable = false)
    private boolean statusState = false;

    @Column(name = "animal_create_time", nullable = false)
    private LocalDateTime createTime;

    @ManyToOne
    @JoinColumn(name = "animal_category_foreign_key")
    private AnimalCategoryEntity animalCategoryFK = new AnimalCategoryEntity();

    @OneToMany(mappedBy = "animalFK", fetch = FetchType.LAZY)
    private List<AnimalIllustrationEntity> animalIllustration = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "account_foreign_key")
    private AccountEntity createdByUser = new AccountEntity();


    public AnimalDTO setAnimal_dto(String fetchImageProfile, String fetchIllustration) {
        AnimalDTO animaldto = new AnimalDTO();
        animaldto.setId(this.getId());
        animaldto.setName(this.getName());
        animaldto.setImageProfile(fetchImageProfile + this.getImageProfile());
        animaldto.setQuantity(this.getQuantity());
        animaldto.setStatusState(this.isStatusState());
        animaldto.setCreateTime(this.getCreateTime());
        animaldto.setAnimalCategoryFK(this.getAnimalCategoryFK().getCategoryName());

        animaldto.setAnimalIllustration(this.getAnimalIllustration().stream().map(
                result -> result.SetAnimalIllustration_dto(fetchIllustration)
        ).collect(Collectors.toList()));

        animaldto.setCreatedByUser(this.getCreatedByUser().getUsername());

        return animaldto;
    }
}
