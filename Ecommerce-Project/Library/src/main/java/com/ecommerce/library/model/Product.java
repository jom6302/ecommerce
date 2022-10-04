package com.ecommerce.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
// 唯一條件約束，以確保不會在未參與主鍵的特定資料行中輸入重複的值
@Table(name = "product", uniqueConstraints = @UniqueConstraint(columnNames = {"name","image"}))
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    private Long id;
    private String name;
    private String description;
    private double costPrice;
    private double salePrice;
    private int currentQuantity;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;
    /*
    * CascadeType.ALL 無論儲存、合併、 更新或移除，一併對被參考物件作出對應動作。
    * FetchType.EAGER， 表示立即從表格取得資料。
    * */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="category_id", referencedColumnName = "category_id")
    private Category category;
    private boolean is_deleted;
    private boolean is_activated;
}
