package br.com.fiap.gsproject.models;

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
@Table(name = "T_SD_LOGIN")
@SequenceGenerator(name = "login", sequenceName = "SQ_TB_SD_LOGIN", allocationSize = 1)
public class Login {
	@Id
	@Column(name = "id_login")
	@GeneratedValue(generator = "login", strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "ds_email", length = 50)
	@NotNull
	private String descricao_email;

	@Column(name = "ds_senha", length = 50)
	@NotNull
	private String descricao_senha;

	@OneToOne
	private CentroDistribuicao centroDistribuicao;

}
