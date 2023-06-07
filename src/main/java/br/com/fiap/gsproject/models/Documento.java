package br.com.fiap.gsproject.models;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import br.com.fiap.gsproject.controllers.DocumentoController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "T_SD_DOCUMENTO")
@SequenceGenerator(name = "documento", sequenceName = "SQ_TB_SD_DOCUMENTO", allocationSize = 1)
public class Documento {
	@Id
	@Column(name = "id_documento")
	@GeneratedValue(generator = "documento", strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "nr_rg", precision = 9)
	@NotNull
	private BigDecimal numero_rg;

	@Column(name = "nr_cpf", precision = 11)
	private BigDecimal numero_cpf;

	public EntityModel<Documento> toEntityModel() {
		List<Link> linkList = new ArrayList<Link>();
		linkList.add(linkTo(methodOn(DocumentoController.class).show(id)).withSelfRel());
		linkList.add(linkTo(methodOn(DocumentoController.class).destroy(id)).withRel("delete"));
		linkList.add(linkTo(methodOn(DocumentoController.class).index(Pageable.unpaged())).withRel("all"));

		return EntityModel.of(
				this,
				linkList);
	}
}