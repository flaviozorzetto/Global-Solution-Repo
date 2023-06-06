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
@Table(name = "T_SD_RECEITA")
@SequenceGenerator(name = "receita", sequenceName = "SQ_TB_SD_RECEITA", allocationSize = 1)
public class Receita {
	@Id
	@Column(name = "id_receita")
	@GeneratedValue(generator = "receita", strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "nm_receita", length = 50)
	@NotNull
	private String nome_receita;

	@Column(name = "ds_preparo", length = 500)
	@NotNull
	private String descricao_preparo;

	@ManyToOne
	private CentroDistribuicao centroDistribuicao;

}
