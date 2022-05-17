package com.hipo.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RelationControllerTest {

//    @Autowired MockMvc mockMvc;
//    @Autowired ObjectMapper objectMapper;
//    @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;
//    @Autowired AccountRepository accountRepository;
//    @Autowired RelationRepository relationRepository;
//    @Autowired JwtProcessor jwtProcessor;
//
//    @BeforeEach
//    public void init() {
//        for (int i = 1; i <= 4; i++) {
//            Account account = new Account("test" + i + "@test.com", bCryptPasswordEncoder.encode("1234"),
//                    "testNickname" + i, "/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg",
//                    Role.User, Gender.MAN, LocalDate.now());
//            accountRepository.save(account);
//        }
//    }
//
//    @Test
//    @DisplayName("친구 요청 성공")
//    public void requestFriendTest() throws Exception {
//
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
//
//        //when
//        //** test1 -> test2 친구 요청
//        mockMvc.perform(post("/relation/request/" + toAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken))
//                .andExpect(status().isOk());
//
//        boolean request = relationRepository
//                .existsByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount, RelationState.REQUEST);
//        boolean toRequest = relationRepository
//                .existsByFromAccountAndToAccountAndRelationStateEquals(toAccount, fromAccount, RelationState.REQUEST);
//
//        //then
//        assertThat(request).isTrue();
//        assertThat(toRequest).isFalse();
//    }
//
//    @Test
//    @DisplayName("친구 요청 성공_상대방만 친구 상태")
//    public void requestFriend_existOpponentFriend_Test() throws Exception {
//
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        relationRepository.save(new Relation(toAccount, fromAccount, RelationState.FRIEND));
//
//        String jwtToken1 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
//
//        //when
//        //** test1 -> test2 친구 요청
//        mockMvc.perform(post("/relation/request/" + toAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken1))
//                .andExpect(status().isOk());
//
//        boolean request = relationRepository
//                .existsByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount, RelationState.REQUEST);
//        boolean toRequest = relationRepository
//                .existsByFromAccountAndToAccountAndRelationStateEquals(toAccount, fromAccount, RelationState.REQUEST);
//
//        //then
//        assertThat(request).isTrue();
//        assertThat(toRequest).isFalse();
//    }
//
//    @Test
//    @DisplayName("친구 요청 실패_상대편에서 친구 요청 한 상태")
//    public void requestFriend_existOpponentRequest_Test() throws Exception {
//
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken1 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
//        String jwtToken2 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(toAccount));
//
//        //** test1 -> test2 친구 요청
//        mockMvc.perform(post("/relation/request/" + toAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken1))
//                .andExpect(status().isOk());
//
//        //when
//        //** test2 -> test1 친구 요청 (상대편쪽에서 이미 친구 요청 한 상태)
//        MvcResult existOpponentRequestResult = mockMvc.perform(post("/relation/request/" + fromAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken2))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        //then
//        BasicErrorResult existOpponentRequestError = objectMapper
//                .readValue(existOpponentRequestResult.getResponse().getContentAsString(), BasicErrorResult.class);
//
//        //then
//        assertThat(existOpponentRequestError.getState()).isEqualTo("400");
//        assertThat(existOpponentRequestError.getException()).isEqualTo(DuplicationRequestException.class.getSimpleName());
//    }
//
//    @Test
//    @DisplayName("친구 요청 실패_친구 요청 중복")
//    public void requestFriend_duplicationRequest_Test() throws Exception {
//
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken1 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
//
//        //** test1 -> test2 친구 요청
//        mockMvc.perform(post("/relation/request/" + toAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken1))
//                .andExpect(status().isOk());
//
//        //when
//        //** test1 -> test2 친구 요청 (친구 요청 중복)
//        MvcResult duplicationRequestResult = mockMvc.perform(post("/relation/request/" + toAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken1))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        //then
//        BasicErrorResult duplicationRequestError = objectMapper
//                .readValue(duplicationRequestResult.getResponse().getContentAsString(), BasicErrorResult.class);
//
//        //then
//        assertThat(duplicationRequestError.getState()).isEqualTo("400");
//        assertThat(duplicationRequestError.getException()).isEqualTo(DuplicationRequestException.class.getSimpleName());
//    }
//
//    @Test
//    @DisplayName("친구 요청 실패_자기 자신에게 친구 요청")
//    public void requestFriend_selfRequest_Test() throws Exception {
//
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken1 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
//
//        //when
//        //** test1 -> test2 친구 요청 (친구 요청 중복)
//        MvcResult selfRequestResult = mockMvc.perform(post("/relation/request/" + fromAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken1))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        //then
//        BasicErrorResult selfRequestError = objectMapper
//                .readValue(selfRequestResult.getResponse().getContentAsString(), BasicErrorResult.class);
//
//        //then
//        assertThat(selfRequestError.getState()).isEqualTo("400");
//        assertThat(selfRequestError.getException()).isEqualTo(IllegalRequestException.class.getSimpleName());
//    }
//
//    @Test
//    @DisplayName("친구 요청 실패_이미 친구 상태")
//    public void requestFriend_existFriend_Test() throws Exception {
//
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        relationRepository.save(new Relation(fromAccount, toAccount, RelationState.FRIEND));
//
//        String jwtToken1 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
//
//        //when
//        //** test1 -> test2 친구 요청 (이미 친구 상태)
//        MvcResult existFriendResult = mockMvc.perform(post("/relation/request/" + fromAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken1))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        //then
//        BasicErrorResult existFriendError = objectMapper
//                .readValue(existFriendResult.getResponse().getContentAsString(), BasicErrorResult.class);
//
//        //then
//        assertThat(existFriendError.getState()).isEqualTo("400");
//        assertThat(existFriendError.getException()).isEqualTo(IllegalRequestException.class.getSimpleName());
//    }
//
//    @Test
//    @DisplayName("친구 요청 실패_차단 상태")
//    public void requestFriend_block_Test() throws Exception {
//
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        relationRepository.save(new Relation(fromAccount, toAccount, RelationState.BLOCK));
//
//        String jwtToken1 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
//
//        //when
//        //** test1 -> test2 친구 요청 (차단 상태)
//        MvcResult illegalRequestResult = mockMvc.perform(post("/relation/request/" + fromAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken1))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        //then
//        BasicErrorResult illegalRequestError = objectMapper
//                .readValue(illegalRequestResult.getResponse().getContentAsString(), BasicErrorResult.class);
//
//        //then
//        assertThat(illegalRequestError.getState()).isEqualTo("400");
//        assertThat(illegalRequestError.getException()).isEqualTo(IllegalRequestException.class.getSimpleName());
//    }
//
//    @Test
//    @DisplayName("친구 요청 수락 성공")
//    public void acceptFriendTest() throws Exception {
//
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken2 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(toAccount));
//
//        relationRepository.save(new Relation(fromAccount, toAccount, RelationState.REQUEST));
//
//        //when
//        mockMvc.perform(post("/relation/request/accept/" + fromAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken2))
//                .andExpect(status().isOk());
//
//        boolean fromFriend = relationRepository
//                .existsByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount, RelationState.FRIEND);
//        boolean toFriend = relationRepository
//                .existsByFromAccountAndToAccountAndRelationStateEquals(toAccount, fromAccount, RelationState.FRIEND);
//
//        //then
//        assertThat(fromFriend).isTrue();
//        assertThat(toFriend).isTrue();
//    }
//
//    @Test
//    @DisplayName("친구 요청 수락 성공_상대방만 친구 상태")
//    public void acceptFriend_existOpponentFriend_Test() throws Exception {
//
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken2 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(toAccount));
//
//        relationRepository.save(new Relation(fromAccount, toAccount, RelationState.REQUEST));
//
//        //when
//        //** test1 -> test2 친구 요청 (test2만 친구 상태)
//        mockMvc.perform(post("/relation/request/accept/" + fromAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken2))
//                .andExpect(status().isOk());
//
//        boolean fromFriend = relationRepository
//                .existsByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount, RelationState.FRIEND);
//        boolean toFriend = relationRepository
//                .existsByFromAccountAndToAccountAndRelationStateEquals(toAccount, fromAccount, RelationState.FRIEND);
//
//        //then
//        assertThat(fromFriend).isTrue();
//        assertThat(toFriend).isTrue();
//    }
//
//    @Test
//    @DisplayName("친구 요청 수락 실패_친구 요청 없음")
//    public void acceptFriend_illegalRequest_Test() throws Exception {
//
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(toAccount));
//
//
//        //when
//        //** test2 -> test1 친구 요청 수락(친구 요청 없음)
//        MvcResult nonExistResourceResult = mockMvc.perform(post("/relation/request/accept/" + fromAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        BasicErrorResult nonExistResourceError = objectMapper
//                .readValue(nonExistResourceResult.getResponse().getContentAsString(), BasicErrorResult.class);
//
//        //then
//        assertThat(nonExistResourceError.getState()).isEqualTo("400");
//        assertThat(nonExistResourceError.getException()).isEqualTo(NonExistResourceException.class.getSimpleName());
//    }
//
//    @Test
//    @DisplayName("친구 요청중 목록 조회_List")
//    public void findAllRequestTest() throws Exception {
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount1 = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount2 = accountRepository.findByUsername("test3@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken1 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
//
//        //** test1 -> test2 친구 요청
//        relationRepository.save(new Relation(fromAccount, toAccount1, RelationState.REQUEST));
//
//        //** test1 -> test3 친구 요청
//        relationRepository.save(new Relation(fromAccount, toAccount2, RelationState.REQUEST));
//
//        //when
//        //** test1의 친구 요청 목록 list
//        MvcResult allListResult = mockMvc.perform(get("/relation/requests")
//                        .param("all", "true")
//                        .header("Authorization", jwtToken1))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        Result<List<AccountDto>> allResult = objectMapper.readValue(allListResult.getResponse().getContentAsString(),
//                new TypeReference<>() {});
//
//        List<AccountDto> requestingList = allResult.getData();
//
//
//        //then
//        //** test1의 친구 요청 목록 size 확인
//        assertThat(requestingList.size()).isEqualTo(2);
//
//        //** test1의 친구 요청 목록 account 확인
//        int i = 2;
//        for (AccountDto accountDto : requestingList) {
//            assertThat(accountDto.getUsername()).isEqualTo("test" + i + "@test.com");
//            i++;
//        }
//    }
//
//    @Test
//    @DisplayName("친구 요청중 목록 조회_Page")
//    public void findRequestsTest() throws Exception {
//
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount1 = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount2 = accountRepository.findByUsername("test3@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken1 = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
//
//        //** test1 -> test2 친구 요청
//        relationRepository.save(new Relation(fromAccount, toAccount1, RelationState.REQUEST));
//
//        //** test1 -> test3 친구 요청
//        relationRepository.save(new Relation(fromAccount, toAccount2, RelationState.REQUEST));
//
//        //when
//        ObjectMapper objectMapper= new ObjectMapper();
//        objectMapper.registerModule(new PageModule());
//        objectMapper.registerModule(new JavaTimeModule());
//
//        //** test1의 친구 요청 목록 page
//        MvcResult pageResult = mockMvc.perform(get("/relation/requests")
//                        .param("page", "1")
//                        .param("size", "1")
//                        .header("Authorization", jwtToken1))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        Page<AccountDto> accountDtoPage = objectMapper.readValue(pageResult.getResponse().getContentAsString(),
//                new TypeReference<>() {
//                });
//        List<AccountDto> content = accountDtoPage.getContent();
//
//        //then
//        //** test1의 친구 요청 목록 page 확인
//        assertThat(accountDtoPage.getTotalElements()).isEqualTo(2);
//        assertThat(content.size()).isEqualTo(1);
//        assertThat(content.get(0).getUsername()).isEqualTo("test3@test.com");
//    }
//
//    @Test
//    @DisplayName("받은 친구 요청 목록 조회_List")
//    public void findAllWaitingRequest() throws Exception {
//
//        //given
//        Account fromAccount1 = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account fromAccount2 = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account fromAccount3 = accountRepository.findByUsername("test3@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount = accountRepository.findByUsername("test4@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(toAccount));
//
//        //** test4 -> test3 차단
//        relationRepository.save(new Relation(toAccount, fromAccount3, RelationState.BLOCK));
//
//        //** test1 -> test4 친구 요청
//        relationRepository.save(new Relation(fromAccount1, toAccount, RelationState.REQUEST));
//
//        //** test2 -> test4 친구 요청
//        relationRepository.save(new Relation(fromAccount2, toAccount, RelationState.REQUEST));
//
//        //** test3 -> test4 친구 요청
//        relationRepository.save(new Relation(fromAccount3, toAccount, RelationState.REQUEST));
//
//        //when
//        //** test4의 전체 친구 요청중 목록 조회
//        MvcResult allListResult = mockMvc.perform(get("/relation/waitRequests")
//                        .header("Authorization", jwtToken)
//                        .param("all", "true"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        Result<List<AccountDto>> allResult = objectMapper.readValue(allListResult.getResponse().getContentAsString(),
//                new TypeReference<>() {});
//        List<AccountDto> waitingRequestList = allResult.getData();
//
//        //then
//        assertThat(waitingRequestList.size()).isEqualTo(2);
//        assertThat(waitingRequestList.get(0).getUsername()).isEqualTo("test1@test.com");
//        assertThat(waitingRequestList.get(1).getUsername()).isEqualTo("test2@test.com");
//    }
//
//    @Test
//    @DisplayName("받은 친구 요청 목록 조회_Page")
//    public void findWaitingRequests() throws Exception {
//
//        //given
//        Account fromAccount1 = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account fromAccount2 = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account fromAccount3 = accountRepository.findByUsername("test3@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount = accountRepository.findByUsername("test4@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(toAccount));
//
//        //** test4 -> test3 차단
//        relationRepository.save(new Relation(toAccount, fromAccount3, RelationState.BLOCK));
//
//        //** test1 -> test4 친구 요청
//        relationRepository.save(new Relation(fromAccount1, toAccount, RelationState.REQUEST));
//
//        //** test2 -> test4 친구 요청
//        relationRepository.save(new Relation(fromAccount2, toAccount, RelationState.REQUEST));
//
//        //** test3 -> test4 친구 요청
//        relationRepository.save(new Relation(fromAccount3, toAccount, RelationState.REQUEST));
//
//        //when
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new PageModule());
//        objectMapper.registerModule(new JavaTimeModule());
//
//        //** test3의 친구 요청중 목록 page 조회
//        MvcResult pageResult = mockMvc.perform(get("/relation/waitRequests")
//                        .header("Authorization", jwtToken)
//                        .param("page", "1")
//                        .param("size", "1"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        Page<AccountDto> accountDtoPage = objectMapper.readValue(pageResult.getResponse().getContentAsString(),
//                new TypeReference<>() {});
//        List<AccountDto> content = accountDtoPage.getContent();
//
//        //then
//        assertThat(accountDtoPage.getTotalElements()).isEqualTo(2);
//        assertThat(content.size()).isEqualTo(1);
//        assertThat(content.get(0).getUsername()).isEqualTo("test2@test.com");
//    }
//
//    @Test
//    @DisplayName("친구 목록 조회_List")
//    public void findAllFriend() throws Exception {
//
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount1 = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount2 = accountRepository.findByUsername("test3@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
//
//        //** test1 -> test2 친구
//        relationRepository.save(new Relation(fromAccount, toAccount1, RelationState.FRIEND));
//
//        //** test1 -> test3 친구
//        relationRepository.save(new Relation(fromAccount, toAccount2, RelationState.FRIEND));
//
//        //when
//        //** test1의 전체 친구 요청중 목록 조회
//        MvcResult allListResult = mockMvc.perform(get("/relation/friends")
//                        .header("Authorization", jwtToken)
//                        .param("all", "true"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        Result<List<AccountDto>> allResult = objectMapper.readValue(allListResult.getResponse().getContentAsString(),
//                new TypeReference<>() {});
//        List<AccountDto> friendList = allResult.getData();
//
//        //then
//        assertThat(friendList.size()).isEqualTo(2);
//        assertThat(friendList.get(0).getUsername()).isEqualTo("test2@test.com");
//        assertThat(friendList.get(1).getUsername()).isEqualTo("test3@test.com");
//    }
//
//    @Test
//    @DisplayName("친구 목록 조회_Page")
//    public void findFriends() throws Exception {
//
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount1 = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount2 = accountRepository.findByUsername("test3@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
//
//        //** test1 -> test2 친구
//        relationRepository.save(new Relation(fromAccount, toAccount1, RelationState.FRIEND));
//
//        //** test1 -> test3 친구
//        relationRepository.save(new Relation(fromAccount, toAccount2, RelationState.FRIEND));
//
//        //when
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new PageModule());
//        objectMapper.registerModule(new JavaTimeModule());
//
//        //** test1의 친구 요청중 목록 page 조회
//        MvcResult pageResult = mockMvc.perform(get("/relation/friends")
//                        .header("Authorization", jwtToken)
//                        .param("page", "1")
//                        .param("size", "1"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        Page<AccountDto> accountDtoPage = objectMapper.readValue(pageResult.getResponse().getContentAsString(),
//                new TypeReference<>() {});
//        List<AccountDto> content = accountDtoPage.getContent();
//
//        //then
//        assertThat(accountDtoPage.getTotalElements()).isEqualTo(2);
//        assertThat(content.size()).isEqualTo(1);
//        assertThat(content.get(0).getUsername()).isEqualTo("test3@test.com");
//    }
//
//    @Test
//    @DisplayName("차단 성공")
//    public void blockAccount() throws Exception {
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
//
//        //when
//        //** test1 -> test2 차단
//        mockMvc.perform(post("/relation/block/" + toAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken))
//                .andExpect(status().isOk());
//
//        boolean isBlock = relationRepository
//                .existsByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount, RelationState.BLOCK);
//
//        //then
//        assertThat(isBlock).isTrue();
//    }
//
//    @Test
//    @DisplayName("차단 성공_서로 친구 상태일 때")
//    public void blockAccount_friendState() throws Exception {
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
//
//        relationRepository.save(new Relation(fromAccount, toAccount, RelationState.FRIEND));
//        relationRepository.save(new Relation(toAccount, fromAccount, RelationState.FRIEND));
//
//        //when
//        //** test1 -> test2 차단
//        mockMvc.perform(post("/relation/block/" + toAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken))
//                .andExpect(status().isOk());
//
//        boolean isBlock = relationRepository
//                .existsByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount, RelationState.BLOCK);
//        boolean isFriend = relationRepository
//                .existsByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount, RelationState.FRIEND);
//        boolean isToFriend = relationRepository
//                .existsByFromAccountAndToAccountAndRelationStateEquals(toAccount, fromAccount, RelationState.FRIEND);
//
//        //then
//        assertThat(isBlock).isTrue();
//        assertThat(isFriend).isFalse();
//        assertThat(isToFriend).isTrue();
//    }
//
//    @Test
//    @DisplayName("차단 실패_중복된 차단")
//    public void blockAccount_duplicationBlock_Test() throws Exception {
//
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
//
//        //** test1 -> test2 차단
//        relationRepository.save(new Relation(fromAccount, toAccount, RelationState.BLOCK));
//
//        //when
//        //** test1 -> test2 차단 (중복된 차단)
//        MvcResult duplicationRequestResult = mockMvc.perform(post("/relation/block/" + toAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        BasicErrorResult duplicationRequestError =
//                objectMapper.readValue(duplicationRequestResult.getResponse().getContentAsString(), BasicErrorResult.class);
//
//        //then
//        assertThat(duplicationRequestError.getState()).isEqualTo("400");
//        assertThat(duplicationRequestError.getException()).isEqualTo(DuplicationRequestException.class.getSimpleName());
//    }
//
//    @Test
//    @DisplayName("차단 목록 조회_List")
//    public void findBlocks_List_Test() throws Exception {
//
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount1 = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount2 = accountRepository.findByUsername("test3@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
//
//        //** test1 -> test2 차단
//        relationRepository.save(new Relation(fromAccount, toAccount1, RelationState.BLOCK));
//
//        //** test1 -> test3 차단
//        relationRepository.save(new Relation(fromAccount, toAccount2, RelationState.BLOCK));
//
//        //when
//        MvcResult allListResult = mockMvc.perform(get("/relation/blocks")
//                        .header("Authorization", jwtToken)
//                        .param("all", "true"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        Result<List<AccountDto>> allResult =
//                objectMapper.readValue(allListResult.getResponse().getContentAsString(), new TypeReference<>() {});
//        List<AccountDto> blockList = allResult.getData();
//
//        //then
//        assertThat(blockList.size()).isEqualTo(2);
//        assertThat(blockList.get(0).getUsername()).isEqualTo("test2@test.com");
//        assertThat(blockList.get(1).getUsername()).isEqualTo("test3@test.com");
//    }
//
//    @Test
//    @DisplayName("차단 목록 조회_Page")
//    public void findBlocks_Page_Test() throws Exception {
//
//        //given
//        Account fromAccount = accountRepository.findByUsername("test1@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount1 = accountRepository.findByUsername("test2@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//        Account toAccount2 = accountRepository.findByUsername("test3@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 가진 Account를 찾을 수 없습니다."));
//
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(fromAccount));
//
//        //** test1 -> test2 차단
//        relationRepository.save(new Relation(fromAccount, toAccount1, RelationState.BLOCK));
//
//        //** test1 -> test3 차단
//        relationRepository.save(new Relation(fromAccount, toAccount2, RelationState.BLOCK));
//
//        //when
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new PageModule());
//        objectMapper.registerModule(new JavaTimeModule());
//
//
//        MvcResult pageResult = mockMvc.perform(get("/relation/blocks")
//                        .header("Authorization", jwtToken)
//                        .param("page", "1")
//                        .param("size", "1"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        Page<AccountDto> pageAccountDto =
//                objectMapper.readValue(pageResult.getResponse().getContentAsString(), new TypeReference<>() {});
//        List<AccountDto> AccountDtoList = pageAccountDto.getContent();
//
//        //then
//        assertThat(pageAccountDto.getTotalElements()).isEqualTo(2);
//        assertThat(AccountDtoList.get(0).getUsername()).isEqualTo("test3@test.com");
//    }
}