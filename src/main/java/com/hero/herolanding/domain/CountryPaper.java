package com.hero.herolanding.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "countryPapersIdSequence", sequenceName = "countryPaper_seq", allocationSize = 1)
@Getter
@Setter
public class CountryPaper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "countryPapersIdSequence")
	@Column(name = "\"국가별 서류 번호\"")
	private int countryPaperNum;

	@Column(name = "\"최소 나이\"")
	private String countryPaperMinAge;

	@Column(name = "\"서류 유효 기간\"")
	private String countryPaperExpiration;

	@Column(name = "\"서류 제출 방법\"")
	private String countryPaperSubmitType;

	@Column(name = "\"비고\"")
	private String countryPaperNote;

//--------<@ManyToOne / Country>-------------------------------------------------------------------------------------	
	@ManyToOne
	@JoinColumn(name = "\"국가 번호\"")
	private Country country; // 국가 조인 컬럼

	public void setCountry(Country country) {
		this.country = country;
		country.getCountryPapers().add(this);
	}

//--------<@ManyToOne / Papers>-------------------------------------------------------------------------------------	
	@ManyToOne
	@JoinColumn(name = "\"서류 번호\"")
	private Paper paper; // 서류 조인 컬럼

	public void setPaper(Paper paper) {
		this.paper = paper;
		paper.getCountryPapers().add(this);
	}

} // CountryPaper class
