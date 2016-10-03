/*
 * Copyright (c) 2016 Martin Hart under the terms of the MIT licence.
 */
package aqa;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author martinhart
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    aqa.VirtualMachineTest.class,
    aqa.InterpreterTest.class,
    aqa.parser.NullInputProviderTest.class,
    aqa.parser.ParserLiteralsTest.class,
    aqa.parser.ParserStringsTest.class,
    aqa.parser.ParserSubroutinesTest.class,
    aqa.parser.ParserVariablesTest.class,
    aqa.subroutine.ArgumentTest.class,
    aqa.subroutine.ArgumentsTest.class,
    aqa.subroutine.SubroutineTest.class,
    aqa.subroutine.SubroutinesImplTest.class,
    aqa.tokenizer.TokenTest.class,
    aqa.tokenizer.TokensTest.class,
    aqa.tokenizer.TokenSequencerTest.class,
    aqa.tokenizer.TokenizerTest.class,
    aqa.value.ArrayValueTest.class,
    aqa.value.BooleanValueTest.class,
    aqa.value.IntegerValueTest.class,
    aqa.value.RealValueTest.class,
    aqa.value.StringValueTest.class,
    aqa.value.ValueBaseTest.class,
    aqa.value.ValueFactoryTest.class,
    aqa.variable.VariableTest.class,
    aqa.variable.VariablesImplTest.class,
})
public class AllTests {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
