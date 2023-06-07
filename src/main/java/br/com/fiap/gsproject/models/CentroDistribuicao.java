package br.com.fiap.gsproject.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import br.com.fiap.gsproject.controllers.CentroDistribuicaoController;
import br.com.fiap.gsproject.controllers.EnderecoController;
import br.com.fiap.gsproject.controllers.LoginController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "T_SD_CENTRO_DISTRIBUICAO")
@SequenceGenerator(name = "distribuicao", sequenceName = "SQ_TB_SD_CENTRO_DISTRIBUICAO", allocationSize = 1)
public class CentroDistribuicao {
	@Id
	@Column(name = "id_distribuicao")
	@GeneratedValue(generator = "distribuicao", strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "nm_centro_distribuicao", length = 50)
	@NotNull
	private String nome_centro_distribuicao;

	@Column(name = "nr_vagas", precision = 3)
	@NotNull
	private BigDecimal numero_vagas;

	@OneToMany(cascade = CascadeType.MERGE)
	private List<Pessoa> pessoas;

	@OneToOne(cascade = CascadeType.MERGE)
	private Endereco endereco;

	@OneToMany(cascade = CascadeType.MERGE)
	private List<Receita> receitas;

	@OneToOne(cascade = CascadeType.MERGE)
	private Login login;

	public EntityModel<CentroDistribuicao> toEntityModel() {
		List<Link> linkList = new ArrayList<Link>();
		linkList.add(linkTo(methodOn(CentroDistribuicaoController.class).show(id)).withSelfRel());
		linkList.add(linkTo(methodOn(CentroDistribuicaoController.class).destroy(id)).withRel("delete"));
		linkList.add(linkTo(methodOn(CentroDistribuicaoController.class).index(Pageable.unpaged())).withRel("all"));
		linkList.add(
				linkTo(methodOn(CentroDistribuicaoController.class).showPessoasFromCentro(id)).withRel("allPessoasFromCD"));
		linkList
				.add(linkTo(methodOn(CentroDistribuicaoController.class).showReceitasFromCentro(id))
						.withRel("allReceitasFromCD"));

		if (endereco != null) {
			linkList
					.add(linkTo(methodOn(EnderecoController.class).show(this.getEndereco().getId())).withRel("enderecoFromCD"));
		}

		if (login != null) {
			linkList.add(linkTo(methodOn(LoginController.class).show(this.getLogin().getId())).withRel("loginFromCD"));
		}

		return EntityModel.of(
				this,
				linkList);
	}
}
