package br.com.fiap.gsproject.models;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import br.com.fiap.gsproject.controllers.LoginController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "T_SD_LOGIN")
@SequenceGenerator(name = "login", sequenceName = "SQ_TB_SD_LOGIN", allocationSize = 1)
public class Login {
	@Id
	@Column(name = "id_login")
	@GeneratedValue(generator = "login", strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "ds_email", length = 50)
	@Length(max = 50)
	@NotBlank
	private String descricao_email;

	@Column(name = "ds_senha", length = 50)
	@Length(max = 50)
	@NotBlank
	private String descricao_senha;

	public EntityModel<Login> toEntityModel() {
		List<Link> linkList = new ArrayList<Link>();
		linkList.add(linkTo(methodOn(LoginController.class).show(id)).withSelfRel());
		linkList.add(linkTo(methodOn(LoginController.class).destroy(id)).withRel("delete"));
		linkList.add(linkTo(methodOn(LoginController.class).index(Pageable.unpaged())).withRel("all"));

		return EntityModel.of(
				this,
				linkList);
	}

}
