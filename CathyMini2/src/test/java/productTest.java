
import com.cathymini.cathymini2.services.ProductBean;
import javax.persistence.EntityManager;
import org.junit.Before;
import static org.mockito.Mockito.mock;

public class productTest {

    private ProductBean productBean = new ProductBean();

    @Before
    public void initializeDependencies() {
        //productBean = mock(ProductBean.class);
        productBean.manager = mock(EntityManager.class);
    }

    /*@Test
     public void creatingProductPersist() {
        Product p = new Tampon(true, "testName", "Tampfdson", 1f, 7f, "testDescription", "testMarque", "testPicto");
        productBean.addProduct(p);
        verify(productBean.manager, times(1)).persist(p);
    }*/

    /**
     * @Test    public void creatingProduct() {
        productBean.addProduct("testName", "testBrand", 1f, 7f, "Tampon", "testDescription");
        when(productBean.addProduct("testName", "testBrand", 1f, 7f, "Tampon", "testDescription")).thenReturn(null);
        when(productBean.addProduct("testName", "testBrand", 1f, 7f, "Tampon", "testDescription").getName()).thenReturn("testName");
    }*
     */
    /**
     * @Test public void creatingProductPersist() {
     * productBean.addProduct("testName", "testBrand", 1f, 7f, "Tampon",
     * "testDescription"); ArgumentCaptor persistedProduct =
     * ArgumentCaptor.forClass(Tampon.class); verify(productBean.manager,
     * atLeastOnce()).persist(persistedProduct.capture()); persistedProduct.
     * assertTrue("Visit time not set correctly", System.currentTimeMillis() -
     * visitTimeInMillis < TimeUnit.SECONDS.toMillis(1));
     * verify(productBean.manager, times(1)).persist(p);
    }*
     */
}
