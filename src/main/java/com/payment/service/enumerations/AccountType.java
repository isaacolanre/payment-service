package com.payment.service.enumerations;

import lombok.Getter;

@Getter
public enum AccountType {
    FEE("191e9959-7c52-495f-a075-fab950f2cc3a", AccountGroup.INTERNAL),
    CUSTOMER("8214bd04-7050-4e62-ba12-094d72a8f89f", AccountGroup.EXTERNAL),
    LIEN("884b7acc-c703-4a06-b9e4-e0c460693414", AccountGroup.INTERNAL),
    SUSPENSE("fa3f61d0-84d1-431a-b180-497960c4e12f", AccountGroup.EXTERNAL),
    CASH("6bb1f186-73ad-4045-949a-63e31289190a", AccountGroup.EXTERNAL),

    COMMISSION("770b9d47-42a1-4237-8839-be3101b59d03", AccountGroup.EXTERNAL),

    REWARDS("4469ed83-1839-4164-a6a6-c85a18faf6d4", AccountGroup.EXTERNAL),

    VIRTUAL("a895b566-001f-4677-ad4f-a4542a46cdad", AccountGroup.EXTERNAL),

    VAT("f8af7bba-b7c4-4176-ae2f-3e48f3896b37", AccountGroup.INTERNAL),

    NA("2be426a7-d758-40dc-83c7-f87dbaad5011", AccountGroup.INTERNAL);

    private final String publicId;

    private final AccountGroup group;

    AccountType(String publicId, AccountGroup group) {
        this.publicId = publicId;
        this.group = group;
    }
}
