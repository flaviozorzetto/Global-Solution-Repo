package br.com.fiap.gsproject.models;

import java.math.BigDecimal;
import java.util.List;

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
}
