package org.teiid.jboss;

import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ValueService;
import org.teiid.deployers.VDBRepository;
import org.teiid.dqp.internal.datamgr.TranslatorRepository;
import org.teiid.jboss.TeiidServiceNames;
import org.teiid.query.function.SystemFunctionManager;

public class VDBServiceDebug {

    public static void main(String[] args) throws InterruptedException {
    	
//    	debug_1();
    	
    	
    }

	static void debug_1() throws InterruptedException {

		ServiceContainer container = ServiceContainer.Factory.create(); 
    	
    	final TranslatorRepository translatorRepo = new TranslatorRepository();
    	ValueService<TranslatorRepository> translatorService = new ValueService<TranslatorRepository>(
                new org.jboss.msc.value.Value<TranslatorRepository>() {
			@Override
			public TranslatorRepository getValue() throws IllegalStateException, IllegalArgumentException {
				return translatorRepo;
			}
    	});
    	ServiceController<TranslatorRepository> service = container.addService(TeiidServiceNames.TRANSLATOR_REPO, translatorService).install();
    	
    	SystemFunctionManager systemFunctionManager = new SystemFunctionManager();
    	systemFunctionManager.setAllowEnvFunction(false);
    	systemFunctionManager.setClassloader(Thread.currentThread().getContextClassLoader());
    	// VDB repository
    	final VDBRepository vdbRepository = new VDBRepository();
    	vdbRepository.setSystemFunctionManager(systemFunctionManager);
    	VDBRepositoryService vdbRepositoryService = new VDBRepositoryService(vdbRepository);
    	container.addService(TeiidServiceNames.VDB_REPO, vdbRepositoryService).install();
    	
    	final JBossLifeCycleListener shutdownListener = new JBossLifeCycleListener();
    	

    	Thread.sleep(1000);
    	
    	container.dumpServices();
    	
    	VDBDeployer deployer = new VDBDeployer(translatorRepo, vdbRepository, shutdownListener);
    	
       System.out.println(deployer);
	}
    
}
