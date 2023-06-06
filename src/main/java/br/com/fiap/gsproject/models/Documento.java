package br.com.fiap.gsproject.models;

import java.math.BigDecimal;

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

	@OneToOne(cascade = CascadeType.ALL)
	private Pessoa pessoa;
}