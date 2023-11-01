
    create table account (
        addr varchar(42) not null,
        provider_code varchar(30),
        trace_id varchar(60),
        chain_id integer not null,
        type_cd varchar(30),
        primary key (addr, chain_id)
    );

    comment on column account.addr is
        'account address in 40 length hexadecimal with 0x prefix';

    comment on column account.provider_code is
        'tenant identifier';

    comment on column account.trace_id is
        'to trace sync or async interactions with external key custody service when creating this account or generating key-pairs';

    comment on column account.chain_id is
        'chain in which this account is opened';

    comment on column account.type_cd is
        'address type from code table - usually one of EOA, Proxy or other application defined types';

    create table chain (
        id integer not null,
        descr varchar(255),
        endpoint varchar(300),
        explorer_url varchar(300),
        is_valid boolean,
        name varchar(200) not null,
        primary key (id)
    );

    comment on table chain is
        'blockchain network';

    comment on column chain.descr is
        'description for this chain';

    comment on column chain.endpoint is
        'JSON RPC endpoint for this chain';

    comment on column chain.explorer_url is
        'base URL of block explorer for this chain';

    comment on column chain.is_valid is
        'indicates the validity of this chain - Invalid chain is not recommended to connect to';

    comment on column chain.name is
        'chain name';

    create table code (
        dtype varchar(31) not null,
        code varchar(30) not null,
        descr varchar(255),
        name varchar(200) not null,
        primary key (code)
    );

    comment on column code.code is
        'code';

    comment on column code.descr is
        'description';

    comment on column code.name is
        'name';

    create table contract (
        addr varchar(255) not null,
        deploy_tx_hash varchar(255),
        deployed_at date,
        deployer_addr varchar(255),
        chain_id integer not null,
        contr_src_id integer,
        primary key (addr, chain_id)
    );

    comment on column contract.addr is
        'contract address in 40 length hexadecimal with 0x prefix';

    comment on column contract.deploy_tx_hash is
        'deployment transaction hash in 64 length hexadecimal with 0x prefix';

    comment on column contract.deployed_at is
        'when this contract deployed';

    comment on column contract.deployer_addr is
        'deployer who signed the contract deployment transaction';

    comment on column contract.chain_id is
        'chain where this contract is deployed';

    comment on column contract.contr_src_id is
        'contract source of this contract instance';

    create table contract_src (
        id integer not null,
        abi oid,
        created_at date,
        name varchar(200) not null,
        scr_ver varchar(50),
        src_commit_hash varchar(40),
        type varchar(255) not null,
        primary key (id)
    );

    comment on column contract_src.name is
        'contract name';

    comment on column contract_src.type is
        'contract type such as ERC1400 ERC1400_FACTORY or et al';

    create index contract_src_idx1 
       on contract_src (type);

    alter table if exists account 
       add constraint FKrbqftcsnryxxx2abi9avga5ib 
       foreign key (chain_id) 
       references chain;

    alter table if exists account 
       add constraint FKqfv5ra636hx3b6mo6tksxbiu3 
       foreign key (type_cd) 
       references code;

    alter table if exists contract 
       add constraint contract_fk1 
       foreign key (chain_id) 
       references chain;

    alter table if exists contract 
       add constraint contract_fk2 
       foreign key (contr_src_id) 
       references contract_src;

    create table account (
        addr varchar(42) not null,
        created_at date,
        invalid_at date,
        is_valid boolean,
        provider_code varchar(30),
        pub_key varchar(130),
        trace_id varchar(60),
        veiled_prv_key varchar(300),
        chain_id integer not null,
        type_cd varchar(30),
        primary key (addr, chain_id)
    );

    comment on column account.addr is
        'account address in 40 length hexadecimal with 0x prefix';

    comment on column account.provider_code is
        'tenant identifier';

    comment on column account.trace_id is
        'to trace sync or async interactions with external key custody service when creating this account or generating key-pairs';

    comment on column account.chain_id is
        'chain in which this account is opened';

    comment on column account.type_cd is
        'address type from code table - usually one of EOA, Proxy or other application defined types';

    create table chain (
        id integer not null,
        descr varchar(255),
        endpoint varchar(300),
        explorer_url varchar(300),
        is_valid boolean,
        name varchar(200) not null,
        primary key (id)
    );

    comment on table chain is
        'blockchain network';

    comment on column chain.descr is
        'description for this chain';

    comment on column chain.endpoint is
        'JSON RPC endpoint for this chain';

    comment on column chain.explorer_url is
        'base URL of block explorer for this chain';

    comment on column chain.is_valid is
        'indicates the validity of this chain - Invalid chain is not recommended to connect to';

    comment on column chain.name is
        'chain name';

    create table code (
        dtype varchar(31) not null,
        code varchar(30) not null,
        descr varchar(255),
        name varchar(200) not null,
        primary key (code)
    );

    comment on column code.code is
        'code';

    comment on column code.descr is
        'description';

    comment on column code.name is
        'name';

    create table contract (
        addr varchar(255) not null,
        deploy_tx_hash varchar(255),
        deployed_at date,
        deployer_addr varchar(255),
        chain_id integer not null,
        contr_src_id integer,
        primary key (addr, chain_id)
    );

    comment on column contract.addr is
        'contract address in 40 length hexadecimal with 0x prefix';

    comment on column contract.deploy_tx_hash is
        'deployment transaction hash in 64 length hexadecimal with 0x prefix';

    comment on column contract.deployed_at is
        'when this contract deployed';

    comment on column contract.deployer_addr is
        'deployer who signed the contract deployment transaction';

    comment on column contract.chain_id is
        'chain where this contract is deployed';

    comment on column contract.contr_src_id is
        'contract source of this contract instance';

    create table contract_src (
        id integer not null,
        abi oid,
        created_at date,
        name varchar(200) not null,
        scr_ver varchar(50),
        src_commit_hash varchar(40),
        type varchar(255) not null,
        primary key (id)
    );

    comment on column contract_src.name is
        'contract name';

    comment on column contract_src.type is
        'contract type such as ERC1400 ERC1400_FACTORY or et al';

    create index contract_src_idx1 
       on contract_src (type);

    alter table if exists account 
       add constraint FKrbqftcsnryxxx2abi9avga5ib 
       foreign key (chain_id) 
       references chain;

    alter table if exists account 
       add constraint FKqfv5ra636hx3b6mo6tksxbiu3 
       foreign key (type_cd) 
       references code;

    alter table if exists contract 
       add constraint contract_fk1 
       foreign key (chain_id) 
       references chain;

    alter table if exists contract 
       add constraint contract_fk2 
       foreign key (contr_src_id) 
       references contract_src;
