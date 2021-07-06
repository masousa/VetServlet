import br.com.letscode.dao.ClienteDao;
import br.com.letscode.dominio.Cliente;
import br.com.letscode.service.ClienteService;
import br.com.letscode.service.ClienteServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClienteServiceTest {

  @InjectMocks
  private ClienteServiceImpl clienteService;

  @Mock
  private ClienteDao clienteDao;




  @Test
  public void should_insert_a_client_succesfully(){
      Cliente cliente = new Cliente();
      cliente.setCpf("27091914074");
      cliente.setNome("Nome de teste");
      clienteService.inserir(cliente);
      Mockito.verify(clienteDao, Mockito.times(1)).inserirArquivo(cliente);
      Assert.assertNotNull(cliente.getIdentificador());
  }
}
