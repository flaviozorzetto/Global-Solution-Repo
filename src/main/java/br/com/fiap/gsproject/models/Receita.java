package br.com.fiap.gsproject.models;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import br.com.fiap.gsproject.controllers.ReceitaController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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
@Table(name = "T_SD_RECEITA")
@SequenceGenerator(name = "receita", sequenceName = "SQ_TB_SD_RECEITA", allocationSize = 1)
public class Receita {
	@Id
	@Column(name = "id_receita")
	@GeneratedValue(generator = "receita", strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "nm_receita", length = 50)
	@Length(max = 50)
	@NotBlank
	private String nome_receita;

	@Column(name = "ds_preparo", length = 500)
	@Length(max = 500)
	@NotBlank
	private String descricao_preparo;

	public EntityModel<Receita> toEntityModel() {
		List<Link> linkList = new ArrayList<Link>();
		linkList.add(linkTo(methodOn(ReceitaController.class).show(id)).withSelfRel());
		linkList.add(linkTo(methodOn(ReceitaController.class).destroy(id)).withRel("delete"));
		linkList.add(linkTo(methodOn(ReceitaController.class).index(Pageable.unpaged())).withRel("all"));

		return EntityModel.of(
				this,
				linkList);
	}
}
