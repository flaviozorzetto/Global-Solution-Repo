package br.com.fiap.gsproject.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.fiap.gsproject.controllers.DocumentoController;
import br.com.fiap.gsproject.controllers.PessoaController;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "T_SD_PESSOA")
@SequenceGenerator(name = "pessoa", sequenceName = "SQ_TB_SD_PESSOA", allocationSize = 1)
public class Pessoa {
	@Id
	@Column(name = "id_pessoa")
	@GeneratedValue(generator = "pessoa", strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "nm_pessoa", length = 100)
	@NotNull
	private String nomePessoa;

	@Column(name = "vl_altura", precision = 3)
	@NotNull
	private BigDecimal valorAltura;

	@Column(name = "vl_peso", precision = 3)
	@NotNull
	private BigDecimal valorPeso;

	@Column(name = "vl_idade", precision = 3)
	@NotNull
	private BigDecimal valorIdade;

	@OneToOne(cascade = CascadeType.MERGE)
	private Documento documento;

	public EntityModel<Pessoa> toEntityModel() {
		List<Link> linkList = new ArrayList<Link>();
		linkList.add(linkTo(methodOn(PessoaController.class).show(id)).withSelfRel());
		linkList.add(linkTo(methodOn(PessoaController.class).destroy(id)).withRel("delete"));
		linkList.add(linkTo(methodOn(PessoaController.class).index(Pageable.unpaged())).withRel("all"));

		if (documento != null) {
			linkList.add(linkTo(methodOn(DocumentoController.class).show(this.getDocumento().getId())).withRel("documento"));
		}

		return EntityModel.of(
				this,
				linkList);

	}
}