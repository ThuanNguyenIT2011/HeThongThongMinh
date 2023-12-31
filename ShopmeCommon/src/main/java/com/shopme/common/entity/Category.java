package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "Categoies")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 128, nullable = false, unique = true)
	private String name;

	@Column(length = 64, nullable = false, unique = true)
	private String alias;

	@Column(length = 128, nullable = false)
	private String image;

	@Column
	private boolean enable;
	
	@Column(name = "all_parent_ids", length = 256)
	private String allParentIDs;

	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;

	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
	@OrderBy("name asc")
	private Set<Category> children = new HashSet<>();

	public Category() {
	}

	public Category(Integer id) {
		this.id = id;
	}

	public Category(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Category(String name) {
		this.name = name;
		this.alias = name;
		this.image = "default.png";
	}

	public Category(String name, Category parent) {
		this(name);
		this.parent = parent;
	}
	

	public Category(Integer id, String name, String alias) {
		this.id = id;
		this.name = name;
		this.alias = alias;
	}

	@Override
	public String toString() {
		return name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}
	
	

//	@Override
//	public int hashCode() {
//		return Objects.hash(alias, children, enable, id, image, name, parent);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Category other = (Category) obj;
//		return Objects.equals(alias, other.alias) && Objects.equals(children, other.children) && enable == other.enable
//				&& Objects.equals(id, other.id) && Objects.equals(image, other.image)
//				&& Objects.equals(name, other.name) && Objects.equals(parent, other.parent);
//	}

	public String getAllParentIDs() {
		return allParentIDs;
	}

	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
	}

	public static Category CoppyFull(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		copyCategory.setAlias(category.getAlias());
		copyCategory.setImage(category.getImage());
		copyCategory.setEnable(category.isEnable());
		copyCategory.setHasChidren(category.getChildren().size() > 0);
		return copyCategory;
	}
	
	public static Category CopyIdAndName(Category category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		return copyCategory;
	}

	@Transient
	public String getImagePath() {
		if (this.id == null) return "/images/defalt-category.png";
		return "/category-images/" + this.id + "/" + this.image;
	}
	
	@Transient
	private boolean hasChidren;

	public boolean isHasChidren() {
		return hasChidren;
	}

	public void setHasChidren(boolean hasChidren) {
		this.hasChidren = hasChidren;
	}
	
	@Transient
	public String getNameCatUser() {
		return this.name.replaceAll("--", "");
	}
	
	
}
