package br.com.fiap.gsproject.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
	private String nome_pessoa;

	@Column(name = "vl_altura", precision = 3)
	@NotNull
	private BigDecimal valor_altura;

	@Column(name = "vl_peso", precision = 3)
	@NotNull
	private BigDecimal valor_peso;

	@ManyToOne
	private CentroDistribuicao centro_distribuicao;
}