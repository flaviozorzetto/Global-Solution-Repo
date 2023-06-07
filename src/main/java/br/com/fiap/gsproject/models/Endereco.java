package br.com.fiap.gsproject.models;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import br.com.fiap.gsproject.controllers.EnderecoController;

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
@Table(name = "T_SD_ENDERECO")
@SequenceGenerator(name = "endereco", sequenceName = "SQ_TB_SD_ENDERECO", allocationSize = 1)
public class Endereco {
	@Id
	@Column(name = "id_endereco")
	@GeneratedValue(generator = "endereco", strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "nm_logradouro", length = 30)
	@NotNull
	private String nome_logradouro;

	@Column(name = "nr_logradouro", precision = 9)
	@NotNull
	private BigDecimal numero_logradouro;

	@Column(name = "nr_cep", precision = 8)
	@NotNull
	private BigDecimal numero_cep;

	@Column(name = "nm_cidade", length = 50)
	@NotNull
	private String nome_cidade;

	@Column(name = "nm_estado", length = 50)
	@NotNull
	private String nome_estado;

	public EntityModel<Endereco> toEntityModel() {
		List<Link> linkList = new ArrayList<Link>();
		linkList.add(linkTo(methodOn(EnderecoController.class).show(id)).withSelfRel());
		linkList.add(linkTo(methodOn(EnderecoController.class).destroy(id)).withRel("delete"));
		linkList.add(linkTo(methodOn(EnderecoController.class).index(Pageable.unpaged())).withRel("all"));

		return EntityModel.of(
				this,
				linkList);
	}

}
