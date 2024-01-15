package school.sptech.harmonyospringapi.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Administrador")
public class Admin extends Usuario{
}
