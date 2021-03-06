using System;
using System.Collections.Generic;
using System.Linq;

using VendingMachine.Console;

using NUnit.Framework;

using TestUtils;

namespace VendingMachine.Console.Test {
    [TestFixture]
    public class _コンソール実行環境本体に関するTestCase {
        [Test]
        public void _お金投入せずに排出を試みる() {
            var app = new FakeConsoleRunner(
                "eject"
            );
            var expected = new string[] {
                "money is not inserted.",
            };

            var it = expected.GetEnumerator();
            
            app.LogUpdated += (message) => {
                Assert.That(it.MoveNext(), Is.True);
                Assert.That(message, Is.Not.Null.And.Not.Empty);
                Assert.That(message, Is.EqualTo(it.Current));
            };
            
            app.Run();
            
            Assert.That(it.MoveNext(), Is.False);
        }

        [Test]
        public void _お金投入するが購入せずただ排出するだけ() {
            var app = new FakeConsoleRunner(
                "ins 100",
                "ins 10 2",
                "eject"
            );

            var expected = new string[] {
                "money: 100 was received.",
                "money: 20 was received.",
                "10(2), 100(1) was ejected.",
//                "total money is 0.",
            };
            var it = expected.GetEnumerator();

            app.LogUpdated += (message) => {
                Assert.That(it.MoveNext(), Is.True);
                Assert.That(message, Is.Not.Null.And.Not.Empty);
                Assert.That(message, Is.EqualTo(it.Current));
            };

            app.Run();

            Assert.That(it.MoveNext(), Is.False);
        }

        [Test]
        public void _お金投入するが購入せずただ排出するだけ_合間で合計金額確認() {
            var app = new FakeConsoleRunner(
                "show amount",
                "ins 100",
                "show amount",
                "ins 10 2",
                "show amount",
                "eject",
                "show amount"
            );
            
            var expected = new string[] {
                "Not received.",
                "money: 100 was received.",
                "total money is 100.",
                "money: 20 was received.",
                "total money is 120.",
                "10(2), 100(1) was ejected.",
                "Not received.",
            };
            var it = expected.GetEnumerator();
            
            app.LogUpdated += (message) => {
                Assert.That(it.MoveNext(), Is.True);
                Assert.That(message, Is.Not.Null.And.Not.Empty);
                Assert.That(message, Is.EqualTo(it.Current));
            };
            
            app.Run();
            
            Assert.That(it.MoveNext(), Is.False);
        }

        [Test]
        public void _ヘルプ表示依頼() {
            var app = new FakeConsoleRunner(
                "help"
            );
            
            var it = ConsoleTestHelper.ListExpectedHelpContents().GetEnumerator();
            
            app.LogUpdated += (message) => {
                Assert.That(it.MoveNext(), Is.True);
                Assert.That(message, Is.Not.Null.And.Not.Empty);
                Assert.That(message, Is.EqualTo(it.Current));
            };
            
            app.Run();
            
            Assert.That(it.MoveNext(), Is.False);
        }

        [Test]
        public void _ヘルプ表示依頼_コマンド指定() {
            var app = new FakeConsoleRunner(
                "help eject"
            );
            
            var it = (new string[] { "eject", "To eject inserted money is requested."}).GetEnumerator();
            
            app.LogUpdated += (message) => {
                Assert.That(it.MoveNext(), Is.True);
                Assert.That(message, Is.Not.Null.And.Not.Empty);
                Assert.That(message, Is.EqualTo(it.Current));
            };
            
            app.Run();
            
            Assert.That(it.MoveNext(), Is.False);
        }
     }

    internal class FakeConsoleRunner : AbstractApplicationRunner {
        private IEnumerator<string> mCommandItertor;

        public FakeConsoleRunner(params string[] inCommands) 
            : base (new Ninject.StandardKernel().BindPurchaseContext().BindRunnerRepository())
        {
            mCommandItertor = inCommands.AsEnumerable()
                .GetEnumerator()
            ;
        }

        protected override string ReadCommand() {
            if (mCommandItertor.MoveNext()) {
                return mCommandItertor.Current;
            }
            else {
                return TerminateCommand; // Ctrl + C
            }
        }
    }
}

